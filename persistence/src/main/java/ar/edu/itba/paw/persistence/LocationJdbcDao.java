package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;

@Repository
public class LocationJdbcDao implements LocationDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Location> ROWMAPPERLOCATION=
            (rs, rowNum) -> new Location(
                    rs.getLong("locationId"),
                    rs.getString("locationString")
            );


    public LocationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public String getLocationByPublicationId(long pubId) {
        return jdbcTemplate.query("SELECT locationString FROM location l JOIN publication p ON l.locationid = p.locationid WHERE p.publicationId = ?",
                new Object[]{ pubId }, new int[]{Types.BIGINT }, ROWMAPPERLOCATION).stream().findFirst().get().getLocationString();
    }
}


