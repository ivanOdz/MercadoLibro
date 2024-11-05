package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.BookBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.*;

@Repository
@Primary
public class BookJpaDao implements BookDao {

    @Autowired
    private MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Book createBook(BookModel bookModel, User owner, BookState bookState) {
        final Book book = new Book(null, owner, bookModel, bookState, 0, true, new ArrayList<>());
        em.persist(book);
        em.flush();
        if (book == null){
            throw new BookBadRequestException(messageSource.getMessage("error.bookCreation", null, LocaleContextHolder.getLocale()));
        }
        return book;
    }

    @Transactional
    @Override
    public void createBookRating(User user, BookModel bookModel, int rating) {
        final BookRating br = new BookRating(user.getUserId(), bookModel.getBookModelId(), rating);
        em.persist(br);
    }

    @Override
    @Transactional
    public void createBookImage(Book book, List<Image> images) {
        for (int i = 0; i < images.size(); i++) {
            final BookImage image = new BookImage(book, i, images.get(i) , Timestamp.valueOf(LocalDateTime.now()));
            em.persist(image);
        }
    }

    @Transactional
    @Override
    public void setOwner(Book book, User user) {
        Book b = em.find(Book.class, book.getBookId());
        b.setOwner(user);
    }

    @Override
    public Book getBookById(long bookId) {

        Book book =  em.find(Book.class, bookId);  // devuelve null si no existe
        if (book == null) {
            throw new BookNotFoundException(messageSource.getMessage("error.bookNotFound", new Object[]{ bookId }, LocaleContextHolder.getLocale()));
        }
        return book;
    }


    @Override
    public List<Book> getAllBooksByUser(long userId) {

        return em.createQuery("from Book as b where b.owner.userId = :userId", Book.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String currentPage, long userId, String sortType) {
        int page;
        try {
            page = Integer.parseInt(currentPage);
            if (page < 0) {
                page = 0;
            }
        } catch (NumberFormatException e) {
            page = 0;
        }

        SortType sort = SortType.fromString(sortType);
        if(sort == null){
            sort = DEFAULT_BOOK_SORT_TYPE;
        }

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT  b.bookId " +
                        "FROM book AS b " +
                        "JOIN users AS u ON b.ownerId = u.userId " +
                        "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
                        "WHERE u.userid = :userId AND LOWER(bm.title) LIKE LOWER(:title)  ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = :genreFilter ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = :bookStateFilter ");
        }

        Query nativeQuery = em.createNativeQuery(sqlQuery.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("title", "%" + safeSearch.toLowerCase() + "%");
        
        if (isGenreFilterActive) {
            nativeQuery.setParameter("genreFilter", genreFilter.toString());
        }

        if (isBookStateFilterActive) {
            nativeQuery.setParameter("bookStateFilter", bookStateFilter.toString());
        }

        nativeQuery.setFirstResult(page * BOOKS_PAGE_SIZE);
        nativeQuery.setMaxResults(BOOKS_PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> bookIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        String jpqlQuery = "FROM Book b WHERE b.bookId IN (:ids)";

        /*switch (sortType) {
            case RATING_ASCENDING:
                jpqlQuery += " ORDER BY bm.averageRating ASC";
                break;
            case RATING_DESCENDING:
                jpqlQuery += " ORDER BY bm.averageRating DESC";
                break;
            case BOOK_NAME_ASCENDING:
                jpqlQuery += " ORDER BY bm.title ASC";
                break;
            default:
                jpqlQuery += " ORDER BY bm.title DESC";
        }*/

        TypedQuery<Book> query = em.createQuery(jpqlQuery, Book.class);
        query.setParameter("ids", bookIds);

        int totalResults = getTotalResultsByBook(safeSearch, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter, userId);

        return new PaginatedResponse<>(query.getResultList(), new ItemFilterMetadata(page, BOOKS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sort, null, isBookStateFilterActive, bookStateFilter, null));
    }


    public List<GenreWrapper> getGenreQtyByBook(String search, boolean isBookStateFilterActive, BookState bookStateFilter, Long userId) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM book b " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = :userId AND LOWER(bm.title) LIKE LOWER(:title) ");

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = :bookState ");
        }

        sqlQuery.append("GROUP BY bm.genre");
/*
       Query query = em.createNativeQuery(sqlQuery.toString())
                .setParameter("userId", userId)
                .setParameter("search", "%" + search.toLowerCase() + "%");
*/
        Query query = em.createNativeQuery(sqlQuery.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        
        query.setParameter("userId", userId);
        query.setParameter("title", "%" + safeSearch.toLowerCase() + "%");
        
        if (isBookStateFilterActive) {
            query.setParameter("bookState", bookStateFilter.toString());
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

    @Transactional
    @Override
    public void setAvailable(Book book, boolean available) {
        Book b = em.find(Book.class, book.getBookId());
        b.setAvailable(available);
    }

    @Override
    @Transactional
    public void saveBookImages(List<BookImage> bookImages) {
        for (BookImage bookImage : bookImages) {
            em.persist(bookImage);
        }
    }

    public List<BookStateWrapper> getBookStateQtyByBook(String search, boolean isGenreFilterActive, Genre genreFilter, Long userId) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT b.bookState, COUNT(*) AS stateCount " +
                        "FROM book b " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = :userId AND LOWER(bm.title) LIKE LOWER(:title) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = :genre ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        Query query = em.createNativeQuery(sqlQuery.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        
        query.setParameter("userId", userId);
        query.setParameter("title", "%" + safeSearch.toLowerCase() + "%");
        
        if (isGenreFilterActive) {
            query.setParameter("genre", genreFilter.toString());
        }

        List<Object[]> results = query.getResultList();

        List<BookStateWrapper> bookStateWrappers = new ArrayList<>();

        for (Object[] result : results) {
            String bookStateValue = result[0].toString();
            BookState bookState = BookState.valueOf(bookStateValue);
            int stateCount = ((Number) result[1]).intValue();
            bookStateWrappers.add(new BookStateWrapper(bookState, stateCount));
        }

        return bookStateWrappers;
    }


    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter,
                                      boolean isBookStateFilterActive, BookState bookStateFilter, long userId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM book b " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = :userId AND LOWER(bm.title) LIKE LOWER(:title) ");

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = :bookState ");
        }

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = :genre ");
        }

        Query query = em.createNativeQuery(sqlQuery.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        
        query.setParameter("userId", userId);
        query.setParameter("title", "%" + safeSearch.toLowerCase() + "%");

        if (isGenreFilterActive) {
            query.setParameter("genre", genreFilter.toString());
        }

        if (isBookStateFilterActive) {
            query.setParameter("bookState", bookStateFilter.toString());
        }

        return ((Number) query.getSingleResult()).intValue();
    }
}
