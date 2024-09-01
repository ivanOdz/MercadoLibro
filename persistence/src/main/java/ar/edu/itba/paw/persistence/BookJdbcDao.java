package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class BookJdbcDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

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

}
