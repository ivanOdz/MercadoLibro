package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Image> ROWMAPPERIMAGE = (rs, rowNum) -> new Image(

            rs.getLong("imageId"),
            rs.getBytes("image")
    );

    public ImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("imageid")
                .withTableName("images");
    }

    @Override
    public Image createImage(byte[] image) {
        final Map<String, byte[]> imageData = Map.of("image", image);
        final Number generatedId = jdbcInsert.executeAndReturnKey(imageData);
        return new Image(generatedId.longValue(), image);
    }

    @Override
    public Optional<Image> getImageById(long imageId) {
        String sql = "SELECT * FROM images WHERE imageId = ?";
        try {
            Image image = jdbcTemplate.queryForObject(sql, new Object[]{imageId}, ROWMAPPERIMAGE);
            return Optional.ofNullable(image);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


}
