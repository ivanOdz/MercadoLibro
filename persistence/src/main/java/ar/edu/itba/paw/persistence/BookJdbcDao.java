package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.persistence.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Book> ROWMAPPERBOOKS = (rs, rowNum) -> new Book(
            rs.getLong("bookId"),
            rs.getString("isbn"),
            rs.getString("title"),
            Arrays.asList((String[]) rs.getArray("authors").getArray()),  // Convertir el array SQL a una lista de Strings
            rs.getString("editorial"),
            rs.getString("description"),
            Genres.fromInt(rs.getInt("genre")),
            PublicationState.fromInt(rs.getInt("publicationState")), // Mapea el valor entero al enum PublicationState
            rs.getInt("edition"),
            rs.getInt("rating"),
            rs.getLong("image"),
            rs.getLong("owner")
    );


    public BookJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Override
    public Optional<Book> getBookById(long thebookId) {
        return jdbcTemplate.query("SELECT * FROM books WHERE bookId = ?", new Object[]{ thebookId },
                new int[]{ Types.BIGINT }, ROWMAPPERBOOKS).stream().findFirst();
    }
}


