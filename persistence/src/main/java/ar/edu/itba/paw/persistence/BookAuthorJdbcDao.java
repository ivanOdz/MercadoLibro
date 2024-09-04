package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Repository
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

        final String sql = "INSERT INTO book_author (bookid, authorid) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookId, authorId);

        System.out.println("bookId2: " + bookId + " authorId2: " + authorId + " segundaChance");
        return new BookAuthor(bookId, authorId);
    }
}
