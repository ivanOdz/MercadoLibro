package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;

import java.util.Set;

public interface LocationService {
    Set<Location> getLocationsByPublicationId(long pubId);

    Location newLocation(String location);

}
