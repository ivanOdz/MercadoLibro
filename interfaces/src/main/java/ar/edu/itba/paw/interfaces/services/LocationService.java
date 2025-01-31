package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;

import java.util.List;

public interface LocationService {
    Location newLocation(String location);

    Location findById(Long locationId);

    List<Location> getLocationByPublicationId(Long publicationId);
}
