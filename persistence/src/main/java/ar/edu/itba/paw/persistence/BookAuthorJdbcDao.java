package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Service
public class BookAuthorJdbcDao implements BookAuthorDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public BookAuthorJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookid").usingGeneratedKeyColumns("authorid").withTableName("book_author");
    }

    @Override
    public BookAuthor createBook_Author(long bookId, long authorId) {
        System.out.println("bookId: " + bookId + " authorId: " + authorId);
        final Map<String, Long> b_aData = Map.of("bookId", bookId, "authorId", authorId);

        final Number generatedId = jdbcInsert.executeAndReturnKey(b_aData);
        System.out.println("bookId: " + bookId + " authorId: " + authorId + "segundaChance");
        return new BookAuthor(bookId, authorId);
    }
}
