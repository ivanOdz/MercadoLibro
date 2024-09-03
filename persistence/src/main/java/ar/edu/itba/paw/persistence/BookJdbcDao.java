package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.sql.Types;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Book> ROWMAPPERBOOKS = (rs, rowNum) -> new Book(
            rs.getLong("bookId"),
            rs.getString("isbn"),
            rs.getString("title"),
            Arrays.asList((String[]) rs.getArray("authors").getArray()),  // Convertir el array SQL a una lista de Strings
            rs.getString("editorial"),
            rs.getString("description"),
            (rs.getInt("genre")),
            rs.getInt("publicationState"), // Mapea el valor entero al enum PublicationState
            rs.getInt("edition"),
            rs.getInt("rating"),
            rs.getLong("image"),
            rs.getLong("owner")
    );


    public BookJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("bookid")
                .withTableName("books");
    }

    @Override
    public Book createBook(String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId) {
        final Map<String, Object> bookData = Map.of();
        bookData.put("isbn", isbn);
        bookData.put("title", title);
        bookData.put("author", String.join(",", author));
        bookData.put("editorial", editorial);
        bookData.put("description", description);
        bookData.put("genre", genre);
        bookData.put("publicationState", publicationState);
        bookData.put("edition", edition);
        bookData.put("rating", rating);
        bookData.put("image", image);
        bookData.put("userId", userId);

        final Number generatedId = jdbcInsert.executeAndReturnKey(bookData);
        return new Book(generatedId.longValue(), isbn, title, author, editorial, description, genre, publicationState, edition, rating, image, userId);
    }

    @Override
    public Optional<Book> getBookById(long thebookId) {
        return jdbcTemplate.query("SELECT * FROM books WHERE bookId = ?", new Object[]{ thebookId },
                new int[]{ Types.BIGINT }, ROWMAPPERBOOKS).stream().findFirst();
    }

    @Override
    public void exchangeOwnership(long b1, long b2) {
        Book book1 = getBookById(b1).get();
        Book book2 = getBookById(b2).get();

        jdbcTemplate.update("UPDATE books SET owner = ? WHERE bookId = ?", book2.getUserId(), book1.getBookId());
        jdbcTemplate.update("UPDATE books SET owner = ? WHERE bookId = ?", book1.getUserId(), book2.getBookId());
    }
}
