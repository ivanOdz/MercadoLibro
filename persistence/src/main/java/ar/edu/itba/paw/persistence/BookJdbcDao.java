package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.BookBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.SQLException;
import java.sql.Types;

import static ar.edu.itba.paw.models.utils.Constants.BOOKS_PAGE_SIZE;
import static ar.edu.itba.paw.persistence.BookModelJdbcDao.ROW_MAPPER_BOOK_MODEL;
import static ar.edu.itba.paw.persistence.UserJdbcDao.ROW_MAPPER_USER;

@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBook;
    private final SimpleJdbcInsert jdbcInsertBookRating;
    private final SimpleJdbcInsert jdbcInsertBookImage;
    private String aggregationFunctionAuthor;
    private String aggregationFunctionImages;
    
    @Autowired
    private MessageSource messageSource;

    static final RowMapper<Book> ROW_MAPPER_BOOK =
            (rs, rowNum) -> {
                User owner = ROW_MAPPER_USER.mapRow(rs, rowNum);
                BookModel bookModel = ROW_MAPPER_BOOK_MODEL.mapRow(rs, rowNum);
                BookState bookState = BookState.fromInt(rs.getInt("bookState"));
                int exchangesQty = rs.getInt("exchangesQty");

                List<Integer> images = rs.getObject("images") == null ? new ArrayList<>() : Arrays.asList((Integer[]) rs.getArray("images").getArray());

                return new Book(rs.getLong("bookId"), owner, bookModel, bookState, exchangesQty, rs.getBoolean("available"), images);
            };

    @Autowired
    public BookJdbcDao(final DataSource ds) throws SQLException {

        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertBook = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").withTableName("book");
        jdbcInsertBookRating = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_rating").usingGeneratedKeyColumns("ratingid");
        jdbcInsertBookImage = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_image");
        
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

        final Map<String, Object> bookData = new HashMap<>();
        bookData.put("bookModelId", bookModelId);
        bookData.put("ownerId", owner.getUserId());
        bookData.put("bookState", bookState.getValue());
        bookData.put("exchangesQty", Constants.INITIAL_EXCHANGE_VALUE);

        return jdbcInsertBook.executeAndReturnKey(bookData);
    }

    @Override
    public void createBookRating(User user, long bookModelId, int rating) {

        String checkQuery = "SELECT COUNT(*) FROM book_rating WHERE userId = ? AND bookModelId = ?";
        int count = jdbcTemplate.queryForObject(checkQuery, new Object[]{user.getUserId(), bookModelId}, Integer.class);

        if (count > 0) {
            String updateQuery = "UPDATE book_rating SET rating = ? WHERE userId = ? AND bookModelId = ?";
            jdbcTemplate.update(updateQuery, rating, user.getUserId(), bookModelId);
        } else {
            final HashMap<String, Object> params = new HashMap<>();
            params.put("userid", user.getUserId());
            params.put("bookModelId", bookModelId);
            params.put("rating", rating);

            jdbcInsertBookRating.execute(params);
        }
    }

    @Override
    public void createBookImage(long bookId, List<Integer> images) {
        int i = 0;
        for (Integer imageId : images) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("bookId", bookId);
            params.put("imageId", imageId);
            params.put("imageOrder", i++);
            params.put("imageDatetime", LocalDateTime.now());
            jdbcInsertBookImage.execute(params);
        }
    }

    @Override
    public void setOwner(long bookId, long userId) {
        try{
            jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", userId, bookId);
        } catch (DataIntegrityViolationException e) {
            throw new BookBadRequestException(messageSource.getMessage("error.settingNewOwner", new Object[]{userId, bookId, e.getStackTrace()}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public Book getBookById(long bookId) {
        String sqlQuery = "SELECT b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, " +
                aggregationFunctionAuthor +  // Verifica que esta variable esté correctamente definida y sea válida en SQL
                "bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
                "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, " + aggregationFunctionImages +  // Verifica que esta variable esté correctamente definida y sea válida en SQL
                "p.publicationState, e.exchangeState, " +
                "CASE " +
                "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
                "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
                "ELSE FALSE " +
                "END AS available " +
                "FROM book AS b " +
                "JOIN users AS u ON b.ownerId = u.userId " +
                "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
                "JOIN book_author AS ba ON ba.bookModelId = bm.bookModelId " +
                "JOIN author AS a ON a.authorId = ba.authorId " +
                "LEFT JOIN (SELECT p1.bookId, p1.publicationId, p1.publicationState FROM publication p1 WHERE p1.publicationDatetime = (SELECT MAX(p2.publicationDatetime) FROM publication p2 WHERE p2.bookId = p1.bookId)) AS p ON p.bookId = b.bookId " + 
                "LEFT JOIN exchange AS e ON e.offererPubId = p.publicationId OR e.requesterPubId = p.publicationId " +
                "LEFT JOIN book_image AS bi ON bi.bookId = b.bookId " +
                "LEFT JOIN book_rating AS br ON bm.bookModelId = br.bookModelId " +
                "LEFT JOIN image AS i ON bi.imageId = i.imageId " +
                "WHERE b.bookId = ? " +
                "GROUP BY available, b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
                "bm.isPocketEdition, bm.isHardcover, p.publicationState, coverId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, e.exchangeState";

        Optional<Book> book = jdbcTemplate.query(sqlQuery, new Object[]{ ExchangeState.ACCEPTED.getValue(), bookId },
                new int[]{ Types.INTEGER, Types.BIGINT }, ROW_MAPPER_BOOK).stream().findFirst();

        if (book.isEmpty()) {
            throw new BookNotFoundException(messageSource.getMessage("error.bookNotFound", new Object[]{ bookId }, LocaleContextHolder.getLocale()));
        }
        return book.get();
    }


    @Override
    public List<Book> getAllBooksByUser(long userId) {
        String sqlQuery = "SELECT b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
                "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, " +
                aggregationFunctionImages +
                "p.publicationState, e.exchangeState, " +  // Checkear esto, no se si hace falta que este en las tuplas que devuelve
                "CASE " +
                "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
                "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
                "ELSE FALSE " +
                "END AS available " +
                "FROM book AS b " +
                "JOIN users AS u ON b.ownerId = u.userId " +
                "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
                "JOIN book_author AS ba ON ba.bookModelId = bm.bookModelId " +
                "JOIN author AS a ON a.authorId = ba.authorId " +
                "LEFT JOIN (SELECT p1.bookId, p1.publicationId, p1.publicationState FROM publication p1 WHERE p1.publicationDatetime = (SELECT MAX(p2.publicationDatetime) FROM publication p2 WHERE p2.bookId = p1.bookId)) AS p ON p.bookId = b.bookId " + 
                "LEFT JOIN exchange AS e ON e.offererPubId = p.publicationId OR e.requesterPubId = p.publicationId " +
                "LEFT JOIN book_image AS bi ON bi.bookId = b.bookId " +
                "LEFT JOIN book_rating AS br ON bm.bookModelId = br.bookModelId " +
                "LEFT JOIN image AS i ON bi.imageId = i.imageId " +
                "WHERE u.userid = ? " +
                "GROUP BY available, b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
                "bm.isPocketEdition, bm.isHardcover, p.publicationState, coverId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, e.exchangeState";

        return jdbcTemplate.query(sqlQuery, new Object[]{ExchangeState.ACCEPTED.getValue(),userId}, new int[]{Types.INTEGER,Types.BIGINT}, ROW_MAPPER_BOOK);

    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT  b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, " +
                        aggregationFunctionAuthor +
                        "bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
                        "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language ," + aggregationFunctionImages +
                        "p.publicationState, e.exchangeState, " +  // Checkear esto, no se si hace falta que este en las tuplas que devuelve
                        "CASE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
                        "ELSE FALSE " +
                        "END AS available " +
                        "FROM book AS b " +
                        "JOIN users AS u ON b.ownerId = u.userId " +
                        "JOIN book_model AS bm ON bm.bookModelId = b.bookModelId " +
                        "JOIN book_author AS ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author AS a ON a.authorId = ba.authorId " +
                        "LEFT JOIN (SELECT p1.bookId, p1.publicationId, p1.publicationState FROM publication p1 WHERE p1.publicationDatetime = (SELECT MAX(p2.publicationDatetime) FROM publication p2 WHERE p2.bookId = p1.bookId)) AS p ON p.bookId = b.bookId " + 
                        "LEFT JOIN exchange AS e ON e.offererPubId = p.publicationId OR e.requesterPubId = p.publicationId " +
                        "LEFT JOIN book_image AS bi ON bi.bookId = b.bookId " +
                        "LEFT JOIN book_rating AS br ON bm.bookModelId = br.bookModelId " +
                        "LEFT JOIN image AS i ON bi.imageId = i.imageId " +
                        "WHERE u.userid = ? AND LOWER(bm.title) LIKE LOWER(?)  ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY available, b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
                "bm.isPocketEdition, bm.isHardcover, p.publicationState, bm.imageId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, e.exchangeState");

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

        int offset = currentPage * BOOKS_PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        List<Book> data;

        if (isGenreFilterActive && isBookStateFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), bookStateFilter.getValue(), BOOKS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        else if (isGenreFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), BOOKS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        else if (isBookStateFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", bookStateFilter.getValue(), BOOKS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        else data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", BOOKS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);

        int totalResults = getTotalResultsByBook(search, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter, userId);

        return new PaginatedResponse<>(data, new ItemFilterMetadata(currentPage, BOOKS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sortType, null, isBookStateFilterActive, bookStateFilter, null));
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

        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add("%" + search.toLowerCase() + "%");

        if (isBookStateFilterActive) {
            params.add(bookStateFilter.getValue());
        }

        int[] paramTypes;
        if (isBookStateFilterActive) {
            paramTypes = new int[]{Types.BIGINT, Types.VARCHAR, Types.INTEGER};
        } else {
            paramTypes = new int[]{ Types.BIGINT, Types.VARCHAR};
        }

        return jdbcTemplate.query(sqlQuery.toString(), params.toArray(), paramTypes, (rs, rowNum) -> {
            int genreValue = rs.getInt("genre");
            Genre genre = Genre.fromInt(genreValue);
            return new GenreWrapper(genre, rs.getInt("genreCount"));
        });
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

        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add("%" + search.toLowerCase() + "%");

        if (isGenreFilterActive) {
            params.add(genreFilter.getValue());
        }

        int[] paramTypes;
        if (isGenreFilterActive) {
            paramTypes = new int[]{Types.BIGINT, Types.VARCHAR, Types.INTEGER};
        } else {
            paramTypes = new int[]{Types.BIGINT, Types.VARCHAR};
        }

        return jdbcTemplate.query(sqlQuery.toString(), params.toArray(), paramTypes, (rs, rowNum) -> {
            int stateValue = rs.getInt("bookState");
            BookState bookState = BookState.fromInt(stateValue);
            return new BookStateWrapper(bookState, rs.getInt("stateCount"));
        });
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

        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add("%" + search.toLowerCase() + "%");

        if (isBookStateFilterActive) {
            params.add(bookStateFilter.getValue());
        }

        if (isGenreFilterActive) {
            params.add(genreFilter.getValue());
        }

        Integer totalResults = jdbcTemplate.queryForObject(sqlQuery.toString(), params.toArray(), Integer.class);

        return totalResults != null ? totalResults : 0;
    }
}

