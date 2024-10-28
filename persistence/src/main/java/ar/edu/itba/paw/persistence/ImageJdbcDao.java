//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfaces.persistence.ImageDao;
//import ar.edu.itba.paw.models.Image;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.Types;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Repository
//public class ImageJdbcDao implements ImageDao {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final SimpleJdbcInsert jdbcInsert;
//
//    private static final RowMapper<Image> ROWMAPPERIMAGE = (rs, rowNum) -> new Image(
//
//            rs.getLong("imageId"),
//            rs.getBytes("image")
//    );
//
//    @Autowired
//    public ImageJdbcDao(final DataSource ds) {
//
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .usingGeneratedKeyColumns("imageid")
//                .withTableName("image");
//    }
//
//    @Override
//    public Image createImage(byte[] image) {
//
//        final Map<String, byte[]> imageData = Map.of("image", image);
//        final Number generatedId = jdbcInsert.executeAndReturnKey(imageData);
//        return new Image(generatedId.longValue(), image);
//    }
//
//    @Override
//    public Optional<Image> getImageById(Long imageId) {
//
//        String sql = "SELECT * FROM image WHERE imageId = ?";
//
//        List<Image> images = jdbcTemplate.query(
//                sql,
//                new Object[]{ imageId },
//                ROWMAPPERIMAGE
//        );
//
//        return images.stream().findFirst();
//    }
//
//    @Override
//    public Image getFirstImageByBookId(Long bookId) {
//
//        return jdbcTemplate.query("SELECT i.imageId, i.image FROM image i JOIN book_image b ON i.imageId = b.imageId WHERE b.bookId = ? AND imageOrder = ?",
//                new Object[]{ bookId, 0 }, new int[] { Types.BIGINT, Types.INTEGER }, ROWMAPPERIMAGE).stream().findFirst().orElse(null);
//    }
//
//
//}
