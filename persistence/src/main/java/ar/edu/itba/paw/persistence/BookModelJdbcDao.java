package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;

@Repository
public class BookModelJdbcDao implements BookModelDao {

    private final JdbcTemplate jdbcTemplate;

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
            rs.getShort("publicationDate"),
            rs.getBoolean("isPocketEdition"),
            rs.getBoolean("isHardcover")
    );


    public BookModelJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        return jdbcTemplate.query("SELECT * FROM book_model WHERE bookModelId = ?", new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROWMAPPERBOOKMODEL).stream().findFirst().get();
    }
}

















