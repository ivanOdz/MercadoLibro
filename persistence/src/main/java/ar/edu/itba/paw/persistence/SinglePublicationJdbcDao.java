package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.persistence.SinglePublicationDao;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SinglePublicationJdbcDao implements SinglePublicationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public SinglePublicationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("publicationid")
                .withTableName("publication");
    }

    /*@Override
    public Publication createPublication(long bookId, long userId, long locationId) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());

        final Map<String, Object> publicationData = new HashMap<>();
        publicationData.put("bookId", bookId);
        publicationData.put("userId", userId);
        publicationData.put("publicationstate", PublicationState.CURRENT.getValue());
        publicationData.put("publicationDatetime", currentTimestamp);
        publicationData.put("locationId", locationId);

        final Number generatedId = jdbcInsert.executeAndReturnKey(publicationData);
        return new Publication(generatedId.longValue(), bookId, userId, PublicationState.CURRENT, currentTimestamp, locationId);
    }*/

}
