package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.utils.Constants.BOOKS_PAGE_SIZE;

@Primary
@Repository
public class BookModelJpaDao implements BookModelDao {

    @Autowired
    MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    @Override
    public BookModel createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover,
                                boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, long bookCoverId) {

        //TODO: Completar como corresponde campo authors y chequear el rating.
        final BookModel bookModel = new BookModel(null, isbn, title, publisher, description, genre, edition, weight,
              pages, language, dimension, publicationYear, isPocketEdition, isHardcover, null, bookCoverId, new Rating(0, 0));

        em.persist(bookModel);
        return bookModel;

    }

    @Override
    public List<Author> createAuthors(List<String> authors) {
        List<Author> authorsRta = new ArrayList<>();

        for(String author : authors) {
            Author newAuthor = new Author(null, author);
            em.persist(newAuthor);
            em.flush(); // Esto asegura que se genere el ID antes de continuar
            authorsRta.add(newAuthor); // Recupera el ID generado
        }

        return authorsRta;
    }

    // ASK: Preguntar si se puede hacer esto para evitar tener que hacer el modelo BookAuthor que unicamente tiene un insert.
    // Yo creo que como lo unico que se realiza sobre book_author es un insert, no es necesario tener un modelo para eso.
    @Override
    public void createBookAuthors(List<Long> authorsIds, long bookModelId) {
        for (Long authorId : authorsIds) {
            String query = "INSERT INTO book_author (bookModelId, authorId) VALUES (:bookModelId, :authorId)";
            em.createNativeQuery(query)
                    .setParameter("bookModelId", bookModelId)
                    .setParameter("authorId", authorId)
                    .executeUpdate();
        }
    }

    @Override
    public Optional<BookModel> getBookModelByBookModelId(long bookModelId) {
        return Optional.ofNullable(em.find(BookModel.class, bookModelId));
    }

    @Override
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType) {
        if(currentPage < 0){
            currentPage = 0;
        }

        // Metodo de paginacion 1 + 1
        // Primera consulta conseguir mediante una nativeQuery los ids de los libros a recuperar con filtros, orden, limit y offset
        StringBuilder nativeQueryString = new StringBuilder("SELECT bm.bookModelId FROM book_model bm " +
                                    "WHERE LOWER(bm.title) LIKE LOWER(:search) ESCAPE '\\' ");

        if (isGenreFilterActive) {
            nativeQueryString.append("AND bm.genre = :genreFilter ");
        }

        switch (sortType) {
            case RATING_ASCENDING:
                nativeQueryString.append(" ORDER BY rating ASC");
                break;
            case RATING_DESCENDING:
                nativeQueryString.append(" ORDER BY rating DESC");
                break;
            case BOOK_NAME_ASCENDING:
                nativeQueryString.append(" ORDER BY title ASC");
                break;
            default:
                nativeQueryString.append(" ORDER BY title DESC");
        }
        Query nativeQuery = em.createNativeQuery(nativeQueryString.toString(), Long.class);

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        nativeQuery.setParameter("search", safeSearch);
        if (isGenreFilterActive) {
            nativeQuery.setParameter("genreFilter", genreFilter.getKey());
        }
        nativeQuery.setMaxResults(BOOKS_PAGE_SIZE);
        nativeQuery.setFirstResult(currentPage * BOOKS_PAGE_SIZE);

        List<Long> bookModelIds = nativeQuery.getResultList();

        // Segunda consulta recuperar los libros mediante una query JPA pasandole los ids recuperados en la primera consulta
        TypedQuery<BookModel> query = em.createQuery("FROM BookModel bm WHERE bm.bookModelId IN (:ids)", BookModel.class);
        query.setParameter("ids", bookModelIds);

        int totalResults = getTotalResultsByBook(safeSearch, isGenreFilterActive, genreFilter);

        return new PaginatedResponse<>(query.getResultList(), new BookModelMetadata(currentPage, BOOKS_PAGE_SIZE, totalResults, safeSearch, isGenreFilterActive, genreFilter, sortType, null));
    }

    @Override
    public List<GenreWrapper> getGenreQtyByBookModel(String search) {
        String sqlQuery = "SELECT bm.genre, COUNT(*) AS genreCount " +
                "FROM book_model bm " +
                "WHERE LOWER(bm.title) LIKE LOWER(:search) ESCAPE '\\' " +
                "GROUP BY bm.genre";

        // Crear la consulta nativa
        // Aparentemente este warning es por inconsistencias en hibernate
        // https://stackoverflow.com/questions/54109546/entitymanager-createnativequery-does-not-return-a-typed-result

        // ASK: Preguntar si esta bien hacer que createNativeQuery retorne List<Object[]>.
        List<Object[]> results = em.createNativeQuery(sqlQuery)
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList();

        // Mapear los resultados a GenreWrapper
        List<GenreWrapper> genreWrappers = new ArrayList<>();
        for (Object[] result : results) {
            String genreValue = result[0].toString();  // bm.genre (STRING)
            Genre genre = Genre.valueOf(genreValue);
            int genreCount = ((Number) result[1]).intValue();  // genreCount
            genreWrappers.add(new GenreWrapper(genre, genreCount));
        }

        return genreWrappers;
    }

    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM book_model bm " +
                        "WHERE LOWER(bm.title) LIKE LOWER(:search) ESCAPE '\\' ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = :genreFilter ");
        }

        Query query = em.createNativeQuery(sqlQuery.toString());
        query.setParameter("search", "%" + search.toLowerCase() + "%");

        if (isGenreFilterActive) {
            query.setParameter("genreFilter", genreFilter.getValue());
        }

        Number totalResults = (Number) query.getSingleResult();
        return totalResults != null ? totalResults.intValue() : 0;
    }
}
