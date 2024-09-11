package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookModelJdbcDao implements BookModelDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<BookModel> ROWMAPPERBOOKMODEL = (rs, rowNum) -> new BookModel(
            rs.getLong("bookModelId"),
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("editorial"),
            rs.getString("description"),
            Genre.fromInt(rs.getInt("genre")),
            rs.getInt("edition"),
            rs.getInt("weight"),
            rs.getInt("pages"),
            Language.fromInt(rs.getInt("bookLanguage")),
            rs.getInt("dimension"),
            rs.getShort("publicationYear"),
            rs.getBoolean("isPocketEdition"),
            rs.getBoolean("isHardcover")
    );


    public BookModelJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("bookmodelid")
                .withTableName("book_model");
    }


    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        return jdbcTemplate.query("SELECT * FROM book_model WHERE bookModelId = ?", new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROWMAPPERBOOKMODEL).stream().findFirst().get();
    }

    @Override
    public void addBookModel(String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, int language, int dimension, Short publicationYear, boolean pocketEdition, boolean hardcover) {
        final Map<String, Object> md = new HashMap<>();
        md.put("isbn", isbn);
        md.put("title", title);
        md.put("editorial", editorial);
        md.put("description", description);
        md.put("genre", genre.name());
        md.put("edition", edition);
        md.put("weight", weight);
        md.put("pages", pages);
        md.put("booklanguage", language);
        md.put("dimension", dimension);
        md.put("publicationyear", publicationYear);
        md.put("ispocketedition", pocketEdition);
        md.put("ishardcover", hardcover);
        jdbcInsert.execute(md);
    }
}

















