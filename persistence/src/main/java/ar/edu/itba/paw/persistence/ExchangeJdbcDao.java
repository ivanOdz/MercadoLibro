package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

import static ar.edu.itba.paw.persistence.PublicationJdbcDao.ROW_MAPPER_PUBLICATION;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    String baseQuery = "SELECT e.exchangeId, e.exchangeState, e.acceptCode, e.offererReceivedBook, e.requesterReceivedBook, e.exchangeEndDate, e.exchangeStartDate, " +

            //---- offererPub
            "op.publicationId, op.publicationState, op_l.locationId, op_l.locationString, op.publicationDatetime, " +

            //   offerer_book
            "op_b.bookId, op_b.bookState, op_b.exchangesQty, " +
            "(SELECT ARRAY_AGG(op_i.imageId ORDER BY op_bi.imageOrder) FROM book_image op_bi JOIN image op_i ON op_bi.imageId = op_i.imageId WHERE op_bi.bookId = op_b.bookId) AS images, " +
            "       CASE " +
            "           WHEN NOT EXISTS (SELECT 1 FROM publication op_p2 WHERE op_p2.bookId = op_b.bookId) THEN TRUE " +
            "           WHEN NOT EXISTS (SELECT 1 FROM exchange op_e2 JOIN publication op_p2 ON op_e2.offererPubId = op_p2.publicationId OR op_e2.requesterPubId = op_p2.publicationId " +
            "                            WHERE op_p2.bookId = op_b.bookId AND op_e2.exchangeState = 1) THEN TRUE " +
            "           ELSE FALSE " +
            "       END AS available, " +

            //  offerer
            "o.userId, o.username, o.mail, o.password, o.imageId, o.verificationCode, o.isVerified, o.language, " +

            //   offerer_book_model
            "op_bm.bookModelId, op_bm.isbn, op_bm.title, op_bm.editorial, op_bm.description, op_bm.genre, op_bm.edition, op_bm.weight, op_bm.pages, op_bm.bookLanguage, " +
            "op_bm.dimension, op_bm.publicationYear, op_bm.isPocketEdition, op_bm.isHardcover, op_bm.imageId AS coverId, " +
            "(SELECT STRING_AGG(op_a.authorName, ', ') FROM book_author op_ba JOIN author op_a ON op_a.authorId = op_ba.authorId WHERE op_ba.bookModelId = op_bm.bookModelId) AS authors, " +
            "AVG(op_br.rating) as rating, COUNT(op_br.rating) as ratingCount, " +

            //----- requesterPub
            "rp.publicationId AS requester_publicationId, rp.publicationState AS requester_publicationState, rp_l.locationId AS requester_locationId, rp_l.locationString AS requester_locationString, rp.publicationDatetime AS requester_publicationDatetime, " +

            //   requester_book
            "rp_b.bookId AS requester_bookId, rp_b.bookState AS requester_bookState, rp_b.exchangesQty AS requester_exchangesQty, " +
            "(SELECT ARRAY_AGG(rp_i.imageId ORDER BY rp_bi.imageOrder) FROM book_image rp_bi JOIN image rp_i ON rp_bi.imageId = rp_i.imageId WHERE rp_bi.bookId = rp_b.bookId) AS requester_images, " +
            "       CASE" +
            "           WHEN NOT EXISTS (SELECT 1 FROM publication rp_p2 WHERE rp_p2.bookId = rp_b.bookId) THEN TRUE" +
            "           WHEN NOT EXISTS (SELECT 1 FROM exchange rp_e2 JOIN publication rp_p2 ON rp_e2.offererPubId = rp_p2.publicationId OR rp_e2.requesterPubId = rp_p2.publicationId" +
            "                            WHERE rp_p2.bookId = rp_b.bookId AND rp_e2.exchangeState = 1) THEN TRUE" +
            "           ELSE FALSE" +
            "           END AS requester_available, " +

            //   requester
            "r.userId AS requester_userId, r.username AS requester_username, r.mail AS requester_mail, r.password AS requester_password, r.imageId AS requester_imageId, r.verificationCode AS requester_verificationCode, r.isVerified AS requester_isVerified, r.language AS requester_language, " +

            //   requester_book_model
            "rp_bm.bookModelId AS requester_bookModelId, rp_bm.isbn AS requester_isbn, rp_bm.title AS requester_title, rp_bm.editorial AS requester_editorial, rp_bm.description AS requester_description, rp_bm.genre AS requester_genre, rp_bm.edition AS requester_edition, rp_bm.weight AS requester_weight, rp_bm.pages AS requester_pages, rp_bm.bookLanguage AS requester_bookLanguage, rp_bm.dimension AS requester_dimension, rp_bm.publicationYear AS requester_publicationYear, rp_bm.isPocketEdition AS requester_isPocketEdition, rp_bm.isHardcover AS requester_isHardcover, rp_bm.imageId AS requester_coverId, " +
            "(SELECT STRING_AGG(rp_a.authorName, ', ') FROM book_author rp_ba JOIN author rp_a ON rp_a.authorId = rp_ba.authorId WHERE rp_ba.bookModelId = rp_bm.bookModelId) AS requester_authors, " +
            "AVG(rp_br.rating) AS requester_rating, COUNT(rp_br.rating) AS requester_ratingCount " +

            "FROM exchange e " +
            // offerer_joins
            "JOIN publication op ON op.publicationId = e.offererPubId " +
            "JOIN location op_l ON op.locationId = op_l.locationId " +
            "JOIN book op_b ON op_b.bookId = op.bookId " +
            "JOIN users o ON op.userId = o.userId " +
            "LEFT JOIN book_image op_bi ON op_bi.bookId = op_b.bookId " +
            "JOIN book_model op_bm ON op_bm.bookModelId = op_b.bookModelId " +
            "LEFT JOIN (SELECT op_br.bookModelId, AVG(op_br.rating) AS rating, COUNT(op_br.rating) AS ratingCount " +
            "FROM book_rating op_br " +
            "GROUP BY op_br.bookModelId) op_avgRatings ON op_avgRatings.bookModelId = op_bm.bookModelId " +
            "LEFT JOIN book_rating op_br ON op_bm.bookModelId = op_br.bookModelId " +
            // requester_joins
            "JOIN publication rp ON rp.publicationId = e.requesterPubId " +
            "JOIN location rp_l ON rp.locationId = rp_l.locationId " +
            "JOIN book rp_b ON rp_b.bookId = rp.bookId " +
            "LEFT JOIN book_image rp_bi ON rp_bi.bookId = rp_b.bookId " +
            "JOIN users r ON rp.userId = r.userId " +
            "JOIN book_model rp_bm ON rp_bm.bookModelId = rp_b.bookModelId " +
            "LEFT JOIN (SELECT rp_br.bookModelId, AVG(rp_br.rating) AS rating, COUNT(rp_br.rating) AS ratingCount " +
            "FROM book_rating rp_br " +
            "GROUP BY rp_br.bookModelId) rp_avgRatings ON rp_avgRatings.bookModelId = rp_bm.bookModelId " +
            "LEFT JOIN book_rating rp_br ON rp_bm.bookModelId = rp_br.bookModelId ";


    private final String groupQuery = "GROUP BY" +
            "    e.exchangeId, e.exchangeState, e.acceptCode, e.offererReceivedBook, e.requesterReceivedBook, e.exchangeEndDate, e.exchangeStartDate, " +
            "    op.publicationId, op.publicationState, op_l.locationId, op_l.locationString, op.publicationDatetime, " +
            "    op_b.bookId, op_b.bookState, op_b.exchangesQty, " +
            "    op_bm.bookModelId, op_bm.isbn, op_bm.title, op_bm.editorial, op_bm.description, op_bm.genre, op_bm.edition, op_bm.weight, op_bm.pages, op_bm.bookLanguage, " +
            "    op_bm.dimension, op_bm.publicationYear, op_bm.isPocketEdition, op_bm.isHardcover, coverId, " +
            "    o.userId, o.username, o.mail, o.password, o.imageId, o.verificationCode, o.isVerified, o.language, " +
            "    rp.publicationId, rp.publicationState, rp_l.locationId, rp_l.locationString, rp.publicationDatetime, " +
            "    rp_b.bookId, rp_b.bookState, rp_b.exchangesQty, " +
            "    r.userId, r.username, r.mail, r.password, r.imageId, r.verificationCode, r.isVerified, r.language, " +
            "    rp_bm.bookModelId, rp_bm.isbn, rp_bm.title, rp_bm.editorial, rp_bm.description, rp_bm.genre, rp_bm.edition, rp_bm.weight, rp_bm.pages, rp_bm.bookLanguage, " +
            "    rp_bm.dimension, rp_bm.publicationYear, rp_bm.isPocketEdition, rp_bm.isHardcover, requester_coverId " +
            "ORDER BY e.exchangeStartDate DESC";

    private static final RowMapper<Publication> ROW_MAPPER_PUBLICATION_REQUEST =
            (rs, rowNum) -> {
                long id = rs.getLong("requester_publicationId");
                PublicationState publicationState = PublicationState.fromInt(rs.getInt("requester_publicationState"));
                Timestamp dateTime = rs.getTimestamp("requester_publicationDatetime");
                Location location = new Location(rs.getLong("requester_locationId"), rs.getString("requester_locationString"));

                User user = new User(rs.getLong("requester_userId"),
                        rs.getString("requester_username"),
                        rs.getString("requester_mail"),
                        rs.getString("requester_password"),
                        rs.getLong("requester_imageId"),
                        rs.getInt("requester_verificationCode"),
                        rs.getBoolean("requester_isVerified"),
                        rs.getString("requester_language"));

                BookModel bookModel = new BookModel(rs.getLong("requester_bookModelId"),
                        rs.getString("requester_isbn"),
                        rs.getString("requester_title"),
                        rs.getString("requester_editorial"),
                        rs.getString("requester_description"),
                        Genre.fromInt(rs.getInt("requester_genre")),
                        rs.getInt("requester_edition"),
                        rs.getInt("requester_weight"),
                        rs.getInt("requester_pages"),
                        Language.fromInt(rs.getInt("requester_bookLanguage")),
                        rs.getInt("requester_dimension"),
                        rs.getShort("requester_publicationYear"),
                        rs.getBoolean("requester_isPocketEdition"),
                        rs.getBoolean("requester_isHardcover"),
                        rs.getString("requester_authors"),
                        rs.getLong("requester_coverId"),
                        new Rating(rs.getDouble("requester_rating"), rs.getInt("requester_ratingCount")));

                Book book = new Book(rs.getLong("requester_bookId"), user, bookModel, BookState.fromInt(rs.getInt("requester_bookState")), rs.getInt("requester_exchangesQty"), rs.getBoolean("requester_available"), rs.getObject("requester_images") == null ? new ArrayList<>() : Arrays.asList((Integer[]) rs.getArray("requester_images").getArray()));

                return new Publication(id, book, publicationState, dateTime, location);
            };

    private static final RowMapper<Exchange> ROW_MAPPER_EXCHANGE =
            (rs, rowNum) -> {
                Publication offererPub = ROW_MAPPER_PUBLICATION.mapRow(rs, rowNum);
                Publication requesterPub = ROW_MAPPER_PUBLICATION_REQUEST.mapRow(rs, rowNum);
                ExchangeState exchangeState = ExchangeState.fromInt(rs.getInt("exchangeState"));
                return new Exchange(rs.getLong("exchangeId"), offererPub, requesterPub, exchangeState, rs.getLong("acceptCode"), rs.getBoolean("offererReceivedBook"), rs.getBoolean("requesterReceivedBook"), rs.getTimestamp("exchangeStartDate"), rs.getTimestamp("exchangeEndDate"));
            };

    public ExchangeJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("exchangeid")
                .withTableName("exchange");
    }

    @Override
    public Optional<Exchange> createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate) {
        final Map<String, Object> exchangeData = new HashMap<>();
        exchangeData.put("offererPubId", offererPubId);
        exchangeData.put("requesterPubId", requesterPubId);
        exchangeData.put("exchangeState", ExchangeState.PENDING.getValue());
        exchangeData.put("acceptCode", acceptCode);
        exchangeData.put("offererReceivedBook", false);
        exchangeData.put("requesterReceivedBook", false);
        exchangeData.put("exchangeStartDate", startDate);
        exchangeData.put("exchangeEndDate", null);

        Number id = jdbcInsert.executeAndReturnKey(exchangeData);

        return getExchangeById(id.longValue());
    }

    @Override
    public Optional<Exchange> acceptExchange(int acceptCode) {

        StringBuilder sqlQuery = new StringBuilder(baseQuery);
        sqlQuery.append(" WHERE acceptCode = ? ");
        sqlQuery.append(groupQuery);
        Optional<Exchange> ex = jdbcTemplate.query(sqlQuery.toString(), new Object[]{acceptCode},
                new int[]{Types.INTEGER}, ROW_MAPPER_EXCHANGE).stream().findFirst();


        jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.ACCEPTED.getValue(), acceptCode);
        jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE offererPubId = ? AND acceptCode <> ?", ExchangeState.REJECTED.getValue(), ex.get().getOfferer().getPublicationId(), acceptCode);

        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{acceptCode},
                new int[]{Types.INTEGER}, ROW_MAPPER_EXCHANGE).stream().findFirst();
    }


    @Override
    public Optional<Exchange> rejectExchange(int acceptCode) {

        StringBuilder sqlQuery = new StringBuilder(baseQuery);
        sqlQuery.append(" WHERE acceptCode = ? ");
        sqlQuery.append(groupQuery);
        Optional<Exchange> ex = jdbcTemplate.query(sqlQuery.toString(), new Object[]{acceptCode},
                new int[]{Types.INTEGER}, ROW_MAPPER_EXCHANGE).stream().findFirst();

        jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.REJECTED.getValue(), acceptCode);
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{acceptCode},
                new int[]{Types.INTEGER}, ROW_MAPPER_EXCHANGE).stream().findFirst();

    }

    @Override
    public void setEndDate(int acceptCode, Timestamp endDate) {
        jdbcTemplate.update("UPDATE exchange SET exchangeenddate = ? WHERE acceptCode = ?", endDate, acceptCode);

    }

    @Override
    public Optional<Exchange> confirmOfferer(int acceptCode) {
        jdbcTemplate.update("UPDATE exchange SET offererReceivedBook = ? WHERE acceptcode = ?", true, acceptCode);
        return findByAcceptCode(acceptCode);
    }

    @Override
    public Optional<Exchange> confirmRequester(int acceptCode) {
        jdbcTemplate.update("UPDATE exchange SET requesterReceivedBook = ? WHERE acceptcode = ?", true, acceptCode);
        return findByAcceptCode(acceptCode);
    }

    @Override
    public void updateExchangeStatus(int acceptCode, int newStatus) {
        String sql = "UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?";
        jdbcTemplate.update(sql, newStatus, acceptCode);
    }

    @Override
    public Optional<Exchange> findByAcceptCode(int acceptCode) {
        StringBuilder sqlQuery = new StringBuilder(baseQuery);
        sqlQuery.append(" WHERE acceptCode = ? ");
        sqlQuery.append(groupQuery);
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{acceptCode}, new int[]{Types.INTEGER}, ROW_MAPPER_EXCHANGE).stream().findFirst();
    }

    @Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
        StringBuilder sqlQuery = new StringBuilder(baseQuery);
        sqlQuery.append(" WHERE exchangeId = ? ");
        sqlQuery.append(groupQuery);
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{exchangeId},
                new int[]{Types.BIGINT}, ROW_MAPPER_EXCHANGE).stream().findFirst();

    }

    @Override
    public List<Exchange> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, boolean isOfferer) {
        StringBuilder sqlQuery = new StringBuilder(baseQuery);

        if (isOfferer) {
            sqlQuery.append("WHERE o.userId = ? ");
        } else {
            sqlQuery.append("WHERE r.userId = ? ");
        }

        sqlQuery.append("AND e.exchangeState = ? ");
        sqlQuery.append(groupQuery);

        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{anUserId, exchangeState.getValue()}, new int[]{Types.BIGINT, Types.INTEGER}, ROW_MAPPER_EXCHANGE);
    }
}
