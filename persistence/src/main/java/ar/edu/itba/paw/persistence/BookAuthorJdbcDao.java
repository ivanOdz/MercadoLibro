package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AuthorDao;
import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class BookAuthorJdbcDao implements BookAuthorDao {

    private static final RowMapper<BookAuthor> ROWMAPPERBOOKAUTHOR =
            (rs, rowNum) -> new BookAuthor(
                    rs.getLong("bookModelId"),
                    rs.getLong("authorId")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final AuthorDao authorDao;

    public BookAuthorJdbcDao(final DataSource ds, final AuthorDao authorDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("book_author")
                .usingColumns("bookmodelid", "authorid");
        this.authorDao = authorDao;
    }

    @Override
    public BookAuthor createBookAuthor(long bookModelId, long authorId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookModelId", bookModelId);
        parameters.put("authorId", authorId);

        jdbcInsert.execute(parameters);

        return new BookAuthor(bookModelId, authorId);
    }

    @Override
    public List<Author> getAuthorsByBookId(long bookModelId) {
        List<BookAuthor> bookAuthors= jdbcTemplate.query("SELECT * FROM book_author WHERE bookModelId = ?", new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROWMAPPERBOOKAUTHOR);
        List<Long> authorIds = bookAuthors.stream().map(BookAuthor::getAuthorId).toList();
        return authorDao.getAuthorsById(authorIds);
    }
}
