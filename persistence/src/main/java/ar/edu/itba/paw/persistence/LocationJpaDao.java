package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Primary
@Repository
public class LocationJpaDao implements LocationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Location> findById(long locationId) {
    	return Optional.ofNullable(em.find(Location.class, locationId));
    }
    
    @Override
    public Set<Location> getLocationByPublicationId(Long pubId) {
        TypedQuery<Location> query = em.createQuery(
                "SELECT l FROM Location l JOIN l.publications p WHERE p.publicationId = :pubId", Location.class);
        query.setParameter("pubId", pubId);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Location newLocation(String locationString) {
        final Location location = new Location(null, locationString);
        em.persist(location);
        return location;
    }
}


