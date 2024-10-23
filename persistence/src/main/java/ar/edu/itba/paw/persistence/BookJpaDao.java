package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.BookBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import static ar.edu.itba.paw.models.utils.Constants.BOOKS_PAGE_SIZE;
import static ar.edu.itba.paw.persistence.BookModelJdbcDao.ROW_MAPPER_BOOK_MODEL;
import static ar.edu.itba.paw.persistence.UserJdbcDao.ROW_MAPPER_USER;

@Repository
@Primary
public class BookJpaDao implements BookDao {

    @Autowired
    MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private final String aggregationFunctionAuthor;
    private final String aggregationFunctionImages;

    static final RowMapper<Book> ROW_MAPPER_BOOK =
            (rs, rowNum) -> {
                User owner = ROW_MAPPER_USER.mapRow(rs, rowNum);
                BookModel bookModel = ROW_MAPPER_BOOK_MODEL.mapRow(rs, rowNum);
                BookState bookState = BookState.fromInt(rs.getInt("bookState"));
                int exchangesQty = rs.getInt("exchangesQty");

                List<Integer> images = rs.getObject("images") == null ? new ArrayList<>() : Arrays.asList((Integer[]) rs.getArray("images").getArray());

                return new Book(rs.getLong("bookId"), owner, bookModel, bookState, exchangesQty, rs.getBoolean("available"), images);
            };


    public BookJpaDao(final DataSource ds) throws SQLException {

            jdbcTemplate = new JdbcTemplate(ds);
//            jdbcInsertBook = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").withTableName("book");
//            jdbcInsertBookRating = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_rating").usingGeneratedKeyColumns("ratingid");
//            jdbcInsertBookImage = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_image");

            String databaseProductName = ds.getConnection().getMetaData().getDatabaseProductName();

            if (databaseProductName.equalsIgnoreCase("HSQL Database Engine")) {

                this.aggregationFunctionAuthor = "'author' AS authors, ";
                this.aggregationFunctionImages = "null AS images, ";
            }
            else { // databaseProductName.equalsIgnoreCase("PostgreSQL")
                this.aggregationFunctionAuthor = "(SELECT GROUP_CONCAT(a.authorName, ', ') FROM book_author ba JOIN author a ON a.authorId = ba.authorId WHERE ba.bookModelId = bm.bookModelId) AS authors, ";
                this.aggregationFunctionImages = "ARRAY_AGG(i.imageId ORDER BY bi.imageOrder) AS images, ";
            }
    }

    @Override
    public Number createBook(long bookModelId, User owner, BookState bookState, List<Integer> images) {
        final Book book = new Book(null, owner, null, bookState, 0, true, images);
        em.persist(book);
        return book.getBookId();
    }

    @Transactional
    @Override
    public void createBookRating(User user, BookModel bookModel, int rating) {
        TypedQuery<BookRating> query = em.createQuery("from BookRating as br where br.userId = :userId and br.bookModelId = :bookModelId", BookRating.class);
        query.setParameter("userId", user.getUserId());
        query.setParameter("bookModelId", bookModel.getBookModelId());
        //try {
            Optional<BookRating> br = Optional.ofNullable(query.getSingleResult());
        //} catch ()
        if (br.isEmpty()) {
            BookRating bookRating = new BookRating(user.getUserId(), bookModel.getBookModelId(), rating);
            em.persist(bookRating);
        } else {
        //    try {
                br.get().setRating(rating);
        //        em.flush();
        //    }
        }
    }



    @Override
    public void createBookImage(long bookId, List<Integer> images) {


        int i = 0;
        for (Integer imageId : images) {
            final BookImage image = new BookImage(null, i++, imageId , Timestamp.valueOf(LocalDateTime.now()));

            em.persist(image);

//            HashMap<String, Object> params = new HashMap<>();
//            params.put("bookId", bookId);
//            params.put("imageId", imageId);
//            params.put("imageOrder", i++);
//            params.put("imageDatetime", LocalDateTime.now());
//            jdbcInsertBookImage.execute(params);
        }
    }

    @Transactional
    @Override
    public void setOwner(Book book, User user) {
        Book b = em.find(Book.class, book.getBookId());  // NOTE: agregado, verificar catch de excepciones

        b.setOwner(user);

//        try{
//            jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", userId, bookId);
//        } catch (DataIntegrityViolationException e) {
//            throw new BookBadRequestException(messageSource.getMessage("error.settingNewOwner", new Object[]{userId, bookId, e.getStackTrace()}, LocaleContextHolder.getLocale()));
//        }
    }

    @Override
    public Book getBookById(long bookId) {
        return em.find(Book.class, bookId);
//        String sqlQuery = "SELECT b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
//                "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, " +
//                aggregationFunctionAuthor +  // Verifica que esta variable esté correctamente definida y sea válida en SQL
//                "bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
//                "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, " + aggregationFunctionImages +  // Verifica que esta variable esté correctamente definida y sea válida en SQL
//                "p.publicationState, e.exchangeState, " +
//                "CASE " +
//                "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
//                "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
//                "ELSE FALSE " +
//                "END AS available " +
//                "FROM book AS b " +
//                "JOIN users AS u ON b.ownerId = u.userId " +
//                "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
//                "JOIN book_author AS ba ON ba.bookModelId = bm.bookModelId " +
//                "JOIN author AS a ON a.authorId = ba.authorId " +
//                "LEFT JOIN (SELECT p1.bookId, p1.publicationId, p1.publicationState FROM publication p1 WHERE p1.publicationDatetime = (SELECT MAX(p2.publicationDatetime) FROM publication p2 WHERE p2.bookId = p1.bookId)) AS p ON p.bookId = b.bookId " +
//                "LEFT JOIN exchange AS e ON e.offererPubId = p.publicationId OR e.requesterPubId = p.publicationId " +
//                "LEFT JOIN book_image AS bi ON bi.bookId = b.bookId " +
//                "LEFT JOIN book_rating AS br ON bm.bookModelId = br.bookModelId " +
//                "LEFT JOIN image AS i ON bi.imageId = i.imageId " +
//                "WHERE b.bookId = ? " +
//                "GROUP BY available, b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
//                "bm.isPocketEdition, bm.isHardcover, p.publicationState, coverId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, e.exchangeState";
//
//        Optional<Book> book = jdbcTemplate.query(sqlQuery, new Object[]{ ExchangeState.ACCEPTED.getValue(), bookId },
//                new int[]{ Types.INTEGER, Types.BIGINT }, ROW_MAPPER_BOOK).stream().findFirst();
//
//        if (book.isEmpty()) {
//            throw new BookNotFoundException(messageSource.getMessage("error.bookNotFound", new Object[]{ bookId }, LocaleContextHolder.getLocale()));
//        }
//        return book.get();
    }


    @Override
    public List<Book> getAllBooksByUser(long userId) {
        return em.createQuery("from Book as b where b.owner.userId = :userId", Book.class)
                .setParameter("userId", userId)
                .getResultList();
//        String sqlQuery = "SELECT b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
//                "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
//                "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, " +
//                aggregationFunctionImages +
//                "p.publicationState, e.exchangeState, " +  // Checkear esto, no se si hace falta que este en las tuplas que devuelve
//                "CASE " +
//                "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
//                "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
//                "ELSE FALSE " +
//                "END AS available " +
//                "FROM book AS b " +
//                "JOIN users AS u ON b.ownerId = u.userId " +
//                "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
//                "JOIN book_author AS ba ON ba.bookModelId = bm.bookModelId " +
//                "JOIN author AS a ON a.authorId = ba.authorId " +
//                "LEFT JOIN (SELECT p1.bookId, p1.publicationId, p1.publicationState FROM publication p1 WHERE p1.publicationDatetime = (SELECT MAX(p2.publicationDatetime) FROM publication p2 WHERE p2.bookId = p1.bookId)) AS p ON p.bookId = b.bookId " +
//                "LEFT JOIN exchange AS e ON e.offererPubId = p.publicationId OR e.requesterPubId = p.publicationId " +
//                "LEFT JOIN book_image AS bi ON bi.bookId = b.bookId " +
//                "LEFT JOIN book_rating AS br ON bm.bookModelId = br.bookModelId " +
//                "LEFT JOIN image AS i ON bi.imageId = i.imageId " +
//                "WHERE u.userid = ? " +
//                "GROUP BY available, b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
//                "bm.isPocketEdition, bm.isHardcover, p.publicationState, coverId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, e.exchangeState";
//
//        return jdbcTemplate.query(sqlQuery, new Object[]{ExchangeState.ACCEPTED.getValue(),userId}, new int[]{Types.INTEGER,Types.BIGINT}, ROW_MAPPER_BOOK);

    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType) {
/*
        StringBuilder hqlQuery = new StringBuilder(
                "SELECT b FROM Book b " +
                        "JOIN b.owner u " +
                        "JOIN b.bookModel bm " +
                        "JOIN bm.authors a " +
                        "LEFT JOIN b.publications p " +
                        "LEFT JOIN p.exchanges e " +
                        "LEFT JOIN b.bookImages bi " +
                        "LEFT JOIN bi.image i " +
                        "LEFT JOIN bm.bookRatings br " +
                        "WHERE u.userId = :userId " +
                        "AND LOWER(bm.title) LIKE LOWER(:search) "
        );

        if (isGenreFilterActive) {
            hqlQuery.append("AND bm.genre = :genreFilter ");
        }

        if (isBookStateFilterActive) {
            hqlQuery.append("AND b.bookState = :bookStateFilter ");
        }

        hqlQuery.append("GROUP BY b, u, bm, p, e ");

        switch (sortType) {
            case RATING_ASCENDING:
                hqlQuery.append("ORDER BY AVG(br.rating) ASC");
                break;
            case RATING_DESCENDING:
                hqlQuery.append("ORDER BY AVG(br.rating) DESC");
                break;
            case BOOK_NAME_ASCENDING:
                hqlQuery.append("ORDER BY bm.title ASC");
                break;
            default:
                hqlQuery.append("ORDER BY bm.title DESC");
        }

        TypedQuery<Book> query = em.createQuery(hqlQuery.toString(), Book.class);
        query.setParameter("userId", userId);
        query.setParameter("search", "%" + search + "%");

        if (isGenreFilterActive) {
            query.setParameter("genreFilter", genreFilter);
        }

        if (isBookStateFilterActive) {
            query.setParameter("bookStateFilter", bookStateFilter);
        }

        query.setFirstResult((currentPage - 1) * BOOKS_PAGE_SIZE);
        query.setMaxResults(BOOKS_PAGE_SIZE);

        List<Book> books = query.getResultList();

        // Metadata can be calculated here if needed, using similar queries for aggregations (e.g., avg ratings, etc.)

        return new PaginatedResponse<>(books, new ItemFilterMetadata());
        */
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT  b.bookId" +
                        "FROM book AS b " +
                        "JOIN users AS u ON b.ownerId = u.userId " +
                        "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
                        "WHERE u.userid = ? AND LOWER(bm.title) LIKE LOWER(?)  ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = :genreFilter ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = :bookStateFilter ");
        }

        switch (sortType) {
            case RATING_ASCENDING:
                sqlQuery.append(" ORDER BY rating ASC");
                break;
            case RATING_DESCENDING:
                sqlQuery.append(" ORDER BY rating DESC");
                break;
            case BOOK_NAME_ASCENDING:
                sqlQuery.append(" ORDER BY title ASC");
                break;
            default:
                sqlQuery.append(" ORDER BY title DESC");
        }

        Query nativeQuery = em.createNativeQuery(sqlQuery.toString(), Book.class);

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        nativeQuery.setParameter("search", safeSearch);

        if (isGenreFilterActive) {
            nativeQuery.setParameter("genreFilter", genreFilter);
        }

        if (isBookStateFilterActive) {
            nativeQuery.setParameter("bookStateFilter", bookStateFilter);
        }

        nativeQuery.setFirstResult(currentPage * BOOKS_PAGE_SIZE);
        nativeQuery.setMaxResults(BOOKS_PAGE_SIZE);

        List<Long> bookIds = nativeQuery.getResultList();

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN (:ids)", Book.class);
        query.setParameter("ids", bookIds);

        int totalResults = getTotalResultsByBook(safeSearch, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter, userId);

        return new PaginatedResponse<>(query.getResultList(), new ItemFilterMetadata(currentPage, BOOKS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sortType, null, isBookStateFilterActive, bookStateFilter, null));
    }

    public List<GenreWrapper> getGenreQtyByBook(String search, boolean isBookStateFilterActive, BookState bookStateFilter, long userId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM book b " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY bm.genre");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("userId", userId);
        query.setParameter("search", "%" + search.toLowerCase() + "%");

        if (isBookStateFilterActive) {
            query.setParameter("bookState", bookStateFilter.getValue());
        }

        return query.getResultList();
    }

    public List<BookStateWrapper> getBookStateQtyByBook(String search, boolean isGenreFilterActive, Genre genreFilter, long userId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT b.bookState, COUNT(*) AS stateCount " +
                        "FROM book b " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("userId", userId);
        query.setParameter("search", "%" + search.toLowerCase() + "%");

        if (isGenreFilterActive) {
            query.setParameter("genre", genreFilter.getValue());
        }

        return query.getResultList();
    }


    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter,
                                      boolean isBookStateFilterActive, BookState bookStateFilter, long userId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE b.ownerId = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("userId", userId);
        query.setParameter("search", "%" + search.toLowerCase() + "%");

        if (isBookStateFilterActive) {
            query.setParameter("bookState", bookStateFilter.getValue());
        }

        if (isGenreFilterActive) {
            query.setParameter("genre", genreFilter.getValue());
        }

        List queryList = query.getResultList();
        Integer totalResults = queryList.isEmpty() ? null : queryList.size();

        return totalResults != null ? totalResults : 0;
    }
}
