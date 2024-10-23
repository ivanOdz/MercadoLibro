package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;

import java.util.Set;

public interface LocationDao {

    Set<Location> getLocationByPublicationId(long pubId);

    Location newLocation(String location);
}
