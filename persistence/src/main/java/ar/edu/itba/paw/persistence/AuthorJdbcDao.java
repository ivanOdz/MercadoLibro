package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AuthorDao;
import ar.edu.itba.paw.models.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class AuthorJdbcDao implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public AuthorJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("authorid").withTableName("author");
    }

    @Override
    public Author createAuthor(String authorName) {
        final Map<String, Object> authorData = Map.of("authorName", authorName);

        final Number generatedId = jdbcInsert.executeAndReturnKey(authorData);
        return new Author(generatedId.longValue(), authorName);
    }
}
