package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.sql.Types;

@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Book> ROWMAPPERBOOKS = (rs, rowNum) -> new Book(
            rs.getLong("bookId"),
            rs.getLong("bookModelId"),
            rs.getLong("ownerId"),
            BookState.fromInt(rs.getInt("bookState")),
            rs.getInt("exchangesQty"),
            rs.getInt("rating")
    );
    
    public BookJdbcDao(final DataSource ds) {
       
    	jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").withTableName("book");
    }

    @Override
    public Book createBook(long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating) {
        
    	final Map<String, Object> bookData = new HashMap<>();
        bookData.put("bookModelId", bookModelId);
        bookData.put("ownerId", ownerId);
        bookData.put("bookState", bookState.getValue());
        bookData.put("exchangesQty", exchangesQty);
        bookData.put("rating", rating);

        final Number generatedId = jdbcInsert.executeAndReturnKey(bookData);
        return new Book(generatedId.longValue(), bookModelId, ownerId, bookState, exchangesQty, rating);
    }

    @Override
    public Optional<Book> getBookById(long bookId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE bookId = ?", new Object[]{ bookId },
                new int[]{ Types.BIGINT }, ROWMAPPERBOOKS).stream().findFirst();
    }

    @Override
    public void exchangeOwnership(long b1, long b2) {
        Book book1 = getBookById(b1).get();
        Book book2 = getBookById(b2).get();

        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", book2.getOwnerId(), book1.getBookId());
        jdbcTemplate.update("UPDATE book SET ownerId = ? WHERE bookId = ?", book1.getOwnerId(), book2.getBookId());
    }

    @Override
    public Book getBookByPubId(long pubId) {
        return jdbcTemplate.query("SELECT * FROM book b JOIN publication p ON b.bookId = p.bookId WHERE p.publicationid = ?", new Object[]{ pubId }, new int[]{ Types.BIGINT }, ROWMAPPERBOOKS).stream().findFirst().get();
    }

    @Override
    public List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search) {
        return jdbcTemplate.query("SELECT * FROM book WHERE ownerId = ? AND bookModelId IN (SELECT bookModelId FROM book_model WHERE LOWER(title) LIKE LOWER(?))",
                new Object[]{ ownerId, "%" + search.toLowerCase() + "%" }, new int[]{ Types.BIGINT, Types.VARCHAR }, ROWMAPPERBOOKS);
    }
}













