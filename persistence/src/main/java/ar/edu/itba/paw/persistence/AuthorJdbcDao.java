package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AuthorDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AuthorJdbcDao implements AuthorDao {

    private static final RowMapper<Author> ROWMAPPERAUTHOR = (rs, rowNum) -> new Author(rs.getLong("authorid"), rs.getString("authorname"));


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public AuthorJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("authorid").withTableName("author");
    }

    @Override
    public Author createAuthor(String authorName) {
        System.out.println("autorName: " + authorName);
        final Map<String, String> authorData = Map.of("authorName", authorName);

        final Number generatedId = jdbcInsert.executeAndReturnKey(authorData);
        System.out.println("generatedId: " + generatedId);
        return new Author(generatedId.longValue(), authorName);
    }

    @Override
    public List<Author> getAuthorsById(List<Long> authorIds) {
        if (authorIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = String.format("SELECT * FROM author WHERE authorId IN (%s)",
                authorIds.stream().map(id -> "?").collect(Collectors.joining(", ")));

        return jdbcTemplate.query(sql, authorIds.toArray(), ROWMAPPERAUTHOR);
    }

}
