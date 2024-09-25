package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.sql.Types;

import static ar.edu.itba.paw.persistence.BookModelJdbcDao.ROW_MAPPER_BOOK_MODEL;
import static ar.edu.itba.paw.persistence.UserJdbcDao.ROW_MAPPER_USER;


@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final int PAGE_SIZE = 21;

    static final RowMapper<Book> ROW_MAPPER_BOOK =
            (rs, rowNum) -> {
                User owner = ROW_MAPPER_USER.mapRow(rs, rowNum);
                BookModel bookModel = ROW_MAPPER_BOOK_MODEL.mapRow(rs, rowNum);
                BookState bookState = BookState.fromInt(rs.getInt("bookState"));
                int exchangesQty = rs.getInt("exchangesQty");
                boolean available = PublicationState.fromInt(rs.getInt("publicationState")) == PublicationState.CURRENT;
                List<Long> images = Arrays.asList((Long[]) rs.getArray("images").getArray());

                return new Book(rs.getLong("bookId"), owner, bookModel, bookState, exchangesQty, available, images);
            };

    public BookJdbcDao(final DataSource ds) {
       
    	jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").withTableName("book");
    }

    /*@Override
    public Book createBook(long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating) {
        
    	final Map<String, Object> bookData = new HashMap<>();
        bookData.put("bookModelId", bookModelId);
        bookData.put("ownerId", ownerId);
        bookData.put("bookState", bookState.getValue());
        bookData.put("exchangesQty", exchangesQty);
        bookData.put("rating", rating);

        final Number generatedId = jdbcInsert.executeAndReturnKey(bookData);
        return new Book(generatedId.longValue(), bookModelId, ownerId, bookState, exchangesQty, rating);
    }*/

    /*@Override
    public Optional<Book> getBookById(long bookId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE bookId = ?", new Object[]{ bookId },
                new int[]{ Types.BIGINT }, ROW_MAPPER_BOOK).stream().findFirst();
    }*/

    /*@Override
    public void exchangeOwnership(long b1, long b2) {
        Book book1 = getBookById(b1).get();
        Book book2 = getBookById(b2).get();

        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", book2.getOwnerId(), book1.getBookId());
        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", book1.getOwnerId(), book2.getBookId());
    }*/

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
    public List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT  b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, i.imageId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount, " +
                        "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, " +
                        "FROM book " +
                        "JOIN users u ON p.userid = u.userid " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "JOIN book_image bi ON bi.bookId = b.bookId " +
                        "JOIN image i ON bi.imageId = i.imageId " +
                        "ARRAY_AGG(i.imageId ORDER BY bi.imageOrder) AS images " +
                        "WHERE u.userid = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY b.bookId, b.exchangesQty, b.bookState, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, " +
                "bm.isPocketEdition, bm.isHardcover, i.imageId, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified");

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

        if(isGenreFilterActive && isBookStateFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), bookStateFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER  }, ROW_MAPPER_BOOK);
        }
        if(isGenreFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, "%" + search.toLowerCase() + "%", genreFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT,  Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK);
        }
        if(isBookStateFilterActive){
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, "%" + search.toLowerCase() + "%", bookStateFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT,  Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK);
        }
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, "%" + search.toLowerCase() + "%", PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK);
    }

}










