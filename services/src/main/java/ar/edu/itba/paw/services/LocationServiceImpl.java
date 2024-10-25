package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.Location;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationDao locationDao;

    public LocationServiceImpl(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public Set<Location> getLocationsByPublicationId(long pubId) {
        return locationDao.getLocationByPublicationId(pubId);
    }

    @Override
    public Location newLocation(String location) {
        return locationDao.newLocation(location);
    }
}
