package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.PublicationNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.PUBLICATIONS_PAGE_SIZE;

@Primary
@Repository
public class PublicationJpaDao implements PublicationDao {

    @Autowired
    MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    // TODO: Manejo de excepciones.
    /*public Long createPublication(long bookId, PublicationState publicationState, Timestamp publicationDatetime, Set<Location> locations) {
        try {
            // ASK: esta bien hacer esto directamente en vez de llamar al getBookById del BookJpaDao?
            Book book = em.find(Book.class, bookId);
            final Publication publication = new Publication(null, book, publicationState, publicationDatetime, locations);
            em.persist(publication);
            return publication.getPublicationId();
        } catch (BookNotFoundException e) {
            throw new IllegalArgumentException(messageSource.getMessage("book.not.found", null, null));
        } catch (PublicationBadRequestException e) {
            throw new IllegalArgumentException(messageSource.getMessage("publication.creation.error", null, null));
        }
    }*/

    @Override
    public Publication createPublication(long bookId, long userId, List<Location> locations, PublicationState publicationState) {
        Book book = em.find(Book.class, bookId);
        User user = em.find(User.class, userId);
        final Publication publication = new Publication(null, book, user,publicationState, new Timestamp(new Date().getTime()), locations);
        em.persist(publication);
        return publication;
    }

    @Override
    public void terminatePublication(long pubId) {
        Publication publication = em.find(Publication.class, pubId);
        publication.setPublicationState(PublicationState.TERMINATED);
        em.merge(publication);
    }

    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        Optional<Publication> maybePublication = Optional.ofNullable(em.find(Publication.class, publicationId));

        if(maybePublication.isEmpty()){
            String message = messageSource.getMessage("error.publicationNotFound", new Object[]{publicationId}, LocaleContextHolder.getLocale());
            throw new PublicationNotFoundException(message);
        }

        return maybePublication.get();
    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {
        if(currentPage < 0){
            currentPage = 0;
        }

        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT p.publicationid " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if (isGenreFilterActive) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (isBookStateFilterActive) {
            nativeQueryString.append("AND b.bookState = :bookState ");
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
            case BOOK_NAME_DESCENDING:
                nativeQueryString.append(" ORDER BY title DESC");
                break;
            case PUBLICATION_DATE_DESCENDING:
                nativeQueryString.append(" ORDER BY publicationDatetime DESC");
                break;
            default:
                nativeQueryString.append(" ORDER BY publicationDatetime ASC");
        }

        Query nativeQuery = em.createNativeQuery(nativeQueryString.toString());

        nativeQuery.setParameter("publicationState", PublicationState.CURRENT.toString());

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        nativeQuery.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isGenreFilterActive){
            nativeQuery.setParameter("genre", genreFilter.getKey());
        }

        if(isBookStateFilterActive){
            nativeQuery.setParameter("bookState", bookStateFilter.getKey());
        }

        nativeQuery.setMaxResults(PUBLICATIONS_PAGE_SIZE);
        nativeQuery.setFirstResult(currentPage * PUBLICATIONS_PAGE_SIZE);

        List<Long> publicationIds = new ArrayList<>();
        try{
            publicationIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (Exception e){
            e.printStackTrace();
        }

        TypedQuery<Publication> query = em.createQuery("FROM Publication p WHERE p.publicationId IN (:ids)", Publication.class);
        query.setParameter("ids",publicationIds);

        int totalResults = getTotalResultsByBook(safeSearch, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter);

        return new PaginatedResponse<>(query.getResultList(), new ItemFilterMetadata(currentPage, PUBLICATIONS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sortType, null, isBookStateFilterActive, bookStateFilter, null));
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM publication p WHERE p.userId = :userId");

        Query nativeQuery = em.createNativeQuery(query.toString());
        nativeQuery.setParameter("userId", userId);

        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public List<GenreWrapper> getGenreQtyByPublication(String search, boolean isBookStateFilterActive, BookState bookStateFilter) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(isBookStateFilterActive){
            sqlQuery.append("AND b.bookState = :bookState ");
        }

        sqlQuery.append("GROUP BY bm.genre");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isBookStateFilterActive){
            query.setParameter("bookState", bookStateFilter.getKey());
        }

        List<Object[]> results = query.getResultList();

        List<GenreWrapper> genreWrappers = new ArrayList<>();
        for (Object[] result : results) {
            String genreValue = result[0].toString();  // bm.genre (STRING)
            Genre genre = Genre.valueOf(genreValue);
            int genreCount = ((Number) result[1]).intValue();  // genreCount
            genreWrappers.add(new GenreWrapper(genre, genreCount));
        }

        return genreWrappers;
    }


    @Override
    public List<BookStateWrapper> getBookStateQtyByPublication(String search, boolean isGenreFilterActive, Genre genreFilter) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT b.bookState, COUNT(*) AS stateCount " +
                "FROM publication p " +
                "JOIN book b ON p.bookId = b.bookId " +
                "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(isGenreFilterActive){
            sqlQuery.append("AND bm.genre = :genre ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isGenreFilterActive){
            query.setParameter("genre", genreFilter.getKey());
        }

        List<Object[]> results = query.getResultList();

        List<BookStateWrapper> bookStateWrappers = new ArrayList<>();
        for (Object[] result : results) {
            String bookStateValue = result[0].toString();
            BookState bookState = BookState.valueOf(bookStateValue);
            int bookStateCount = ((Number) result[1]).intValue();
            bookStateWrappers.add(new BookStateWrapper(bookState, bookStateCount));
        }

        return bookStateWrappers;
    }


    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter, boolean isBookStateFilterActive, BookState bookStateFilter){
        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if (isGenreFilterActive) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (isBookStateFilterActive) {
            nativeQueryString.append("AND b.bookState = :bookState ");
        }

        Query query = em.createNativeQuery(nativeQueryString.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isGenreFilterActive){
            query.setParameter("genre", genreFilter.getKey());
        }

        if(isBookStateFilterActive){
            query.setParameter("bookState", bookStateFilter.getKey());
        }

        return ((Number) query.getSingleResult()).intValue();
    }
}