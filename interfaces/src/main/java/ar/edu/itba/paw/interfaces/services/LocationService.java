package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;

public interface LocationService {
    Location newLocation(String location);

    Location findById(Long locationId);
}
