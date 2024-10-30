package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.*;

@Primary
@Repository
public class LocationJpaDao implements LocationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @PersistenceContext
    private EntityManager em;

    private static final RowMapper<Location> ROWMAPPERLOCATION=
            (rs, rowNum) -> new Location(
                    rs.getLong("locationid"),
                    rs.getString("locationstring")
            );


    @Autowired
    public LocationJpaDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("location").usingGeneratedKeyColumns("locationid");
    }

    @Override
    public Optional<Location> findById(long locationId) {

    	return Optional.ofNullable(em.find(Location.class, locationId));
    }
    
    @Override
    public Set<Location> getLocationByPublicationId(long pubId) {
        TypedQuery<Location> query = em.createQuery(
                "SELECT l FROM Location l JOIN l.publications p WHERE p.publicationId = :pubId", Location.class);
        query.setParameter("pubId", pubId);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Location newLocation(String locationString) {
        final Location location = new Location(null, locationString);

        // NOTE: consultar por manejo de excepciones en JPA

        /*
        Number userId;
        try {
            userId = jdbcInsert.executeAndReturnKey(userData);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.createUser", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new UserBadRequestException(errorMessage);
        }
        */

        em.persist(location);
        return location;


//        Map<String, Object> params = new HashMap<>();
//        params.put("locationstring", location);
//
//        return jdbcInsert.executeAndReturnKey(params).longValue();
    }
}


