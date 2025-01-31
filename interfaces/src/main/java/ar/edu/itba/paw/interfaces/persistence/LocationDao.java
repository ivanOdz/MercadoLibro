package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;

import java.util.Optional;
import java.util.Set;

public interface LocationDao {

	Optional<Location> findById(long locationId);
	
    Set<Location> getLocationByPublicationId(Long pubId);

    Location newLocation(String location);
}
