package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Types;

import static ar.edu.itba.paw.models.utils.Constants.PAGE_SIZE;
import static ar.edu.itba.paw.persistence.BookModelJdbcDao.ROW_MAPPER_BOOK_MODEL;
import static ar.edu.itba.paw.persistence.UserJdbcDao.ROW_MAPPER_USER;


@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBook;
    private final SimpleJdbcInsert jdbcInsertBookRating;
    private final SimpleJdbcInsert jdbcInsertBookImage;


    static final RowMapper<Book> ROW_MAPPER_BOOK =
            (rs, rowNum) -> {
                User owner = ROW_MAPPER_USER.mapRow(rs, rowNum);
                BookModel bookModel = ROW_MAPPER_BOOK_MODEL.mapRow(rs, rowNum);
                BookState bookState = BookState.fromInt(rs.getInt("bookState"));
                int exchangesQty = rs.getInt("exchangesQty");

                List<Integer> images = Arrays.asList((Integer[]) rs.getArray("images").getArray());

                return new Book(rs.getLong("bookId"), owner, bookModel, bookState, exchangesQty, rs.getBoolean("available"), images);
            };

    public BookJdbcDao(final DataSource ds) {

        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertBook = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").withTableName("book");
        jdbcInsertBookRating = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_rating").usingGeneratedKeyColumns("ratingid");
        jdbcInsertBookImage = new SimpleJdbcInsert(jdbcTemplate).withTableName("book_image");
    }

    /*@Override
    public Optional<Book> getBookById(long bookId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE bookId = ?", new Object[]{ bookId },
                new int[]{ Types.BIGINT }, ROW_MAPPER_BOOK).stream().findFirst();
    }*/

//    @Override
//    public void exchangeOwnership(Book b1, Book b2) {
//        Book book1 = getBookById(b1).get();
//        Book book2 = getBookById(b2).get();
//
//        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", book1.getOwnerId(), book2.getBookId());
//    }


    @Override
    public void setOwner(long bookId, long userId) {
        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", userId, bookId);
    }

    /*@Override
    public Book getBookByPubId(long pubId) {
        return jdbcTemplate.query("SELECT * FROM book b JOIN publication p ON b.bookId = p.bookId WHERE p.publicationid = ?", new Object[]{ pubId }, new int[]{ Types.BIGINT }, ROWMAPPERBOOKS).stream().findFirst().get();
    }*/

    /*@Override
    public List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter) {
        return jdbcTemplate.query("SELECT * FROM book WHERE ownerId = ? AND (? = 6 OR bookstate = ?) AND bookModelId IN (SELECT bookModelId FROM book_model WHERE LOWER(title) LIKE LOWER(?) AND (? = 32 OR genre = ?))",
                new Object[]{ ownerId, bookStateFilter, bookStateFilter, "%" + search.toLowerCase() + "%", genreFilter, genreFilter}, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROWMAPPERBOOKS);
    }*/


    @Override
    public Number createBook(long bookModelId, User owner, BookState bookState, List<Integer> images) {

        final Map<String, Object> bookData = new HashMap<>();
        bookData.put("bookModelId", bookModelId);
        bookData.put("ownerId", owner.getUserId());
        bookData.put("bookState", bookState.getValue());
        bookData.put("exchangesQty", Constants.INITIAL_EXCHANGE_VALUE);

        final Number generatedId = jdbcInsertBook.executeAndReturnKey(bookData);
        return generatedId;
    }

    @Override
    public void createBookRating(User user, long bookModelId, int rating) {

        String checkQuery = "SELECT COUNT(*) FROM book_rating WHERE userId = ? AND bookModelId = ?";
        int count = jdbcTemplate.queryForObject(checkQuery, new Object[]{user.getUserId(), bookModelId}, Integer.class);

        if(count > 0) {
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


    // TODO: AUTORES DUPLICADOS: REEMPLAZAR EL STRING_AGG POR "(SELECT STRING_AGG(a.authorName, ', ') " +
    //                " FROM book_author ba " +
    //                " JOIN author a ON a.authorId = ba.authorId " +
    //                " WHERE ba.bookModelId = bm.bookModelId) AS authors,
    // cuando tengo varias imagenes asociadas a un lbro me trae el nombre del autor x # imagenes del libro


    @Override
    public List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT  b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, bm.imageId AS coverId, AVG(br.rating) AS rating, COUNT(br.rating) AS ratingCount, " +
                        "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, ARRAY_AGG(i.imageId ORDER BY bi.imageOrder) AS images, " +
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
                        "LEFT JOIN (SELECT DISTINCT ON (bookId) * FROM publication ORDER BY bookId, publicationDatetime DESC) AS p ON p.bookId = b.bookId " +
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
                "bm.isPocketEdition, bm.isHardcover, p.publicationState, bm.imageId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, e.exchangeState");

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

        int offset = pageIndex * PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        if (isGenreFilterActive && isBookStateFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), bookStateFilter.getValue(), PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        if (isGenreFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        if (isBookStateFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", bookStateFilter.getValue(), PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
        }
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), userId, "%" + search.toLowerCase() + "%", PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK);
    }
}










