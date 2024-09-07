package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AuthorDao;
import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;
import ar.edu.itba.paw.models.Publication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookAuthorJdbcDao implements BookAuthorDao {

    private static final RowMapper<BookAuthor> ROWMAPPERBOOKAUTHOR =
            (rs, rowNum) -> new BookAuthor(
                    rs.getLong("bookId"),
                    rs.getLong("authorId")
            );

    private final AuthorDao ad;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public BookAuthorJdbcDao(final DataSource ds, final AuthorDao ad) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("bookId").usingGeneratedKeyColumns("authorId").withTableName("book_author");
        this.ad = ad;
    }

    @Override
    public BookAuthor createBook_Author(long bookId, long authorId) {
        final String sql = "INSERT INTO book_author (bookid, authorid) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookId, authorId);

        return new BookAuthor(bookId, authorId);
    }

    @Override
    public List<Author> getAuthorsByBookId(long bookId) {
        List<BookAuthor> bookAuthors= jdbcTemplate.query("SELECT * FROM book_author WHERE bookId = ?", new Object[]{ bookId }, new int[]{Types.BIGINT}, ROWMAPPERBOOKAUTHOR);
        List<Long> authorIds = bookAuthors.stream().map(BookAuthor::getAuthorId).toList();
        return ad.getAuthorsById(authorIds);
    }
}
