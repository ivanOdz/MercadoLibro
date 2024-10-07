package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LocationJdbcDao implements LocationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Location> ROWMAPPERLOCATION=
            (rs, rowNum) -> new Location(
                    rs.getLong("locationid"),
                    rs.getString("locationstring")
            );


    @Autowired
    public LocationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("location").usingGeneratedKeyColumns("locationid");
    }

    @Override
    public String getLocationByPublicationId(long pubId) {
        return jdbcTemplate.query("SELECT * FROM location NATURAL JOIN publication WHERE publicationId = ?",
                new Object[]{ pubId }, new int[]{ Types.BIGINT }, ROWMAPPERLOCATION).stream().findFirst().get().getLocationString();
    }

    @Override
    public long newLocation(String location) {
        Map<String, Object> params = new HashMap<>();
        params.put("locationstring", location);

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }
}


