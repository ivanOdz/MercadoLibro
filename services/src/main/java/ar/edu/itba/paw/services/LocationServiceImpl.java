package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationDao locationDao;

    public LocationServiceImpl(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public String getLocationByPublicationId(long pubId) {
        return locationDao.getLocationByPublicationId(pubId);
    }
}
