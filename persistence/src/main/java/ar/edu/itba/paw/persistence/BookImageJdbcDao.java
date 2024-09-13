package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookImageDao;
import ar.edu.itba.paw.models.BookImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class BookImageJdbcDao implements BookImageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<BookImage> ROWMAPPERBOOKIMAGE = (rs, rowNum) -> new BookImage(

            rs.getLong("bookId"),
            rs.getInt("imageOrder"),
            rs.getLong("imageId"),
            rs.getTimestamp("imageDatetime")
    );

    public BookImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("book_image")
                .usingColumns("bookId", "imageOrder", "imageId", "imageDatetime");
    }

    @Override
    public List<BookImage> getImageByBookId(long bookId) {
        return jdbcTemplate.query(
                "SELECT * FROM book_image WHERE bookId = ? ORDER BY imageOrder ASC",
                new Object[]{ bookId }, new int[]{ Types.BIGINT }, ROWMAPPERBOOKIMAGE);
    }

    @Override
    public void saveBookImage(long bookId, int imageOrder, long imageId, Timestamp timestamp) {
        Map<String, Object> params = new HashMap<>();

        params.put("bookId", bookId);
        params.put("imageOrder", imageOrder);
        params.put("imageId", imageId);
        params.put("imageDatetime", timestamp);

        jdbcInsert.execute(params);
    }


}

