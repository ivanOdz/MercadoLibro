package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.LocationNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;


    @Override
    @Transactional(readOnly = true)
    public Set<Location> getLocationsByPublicationId(long pubId) {
        return locationDao.getLocationByPublicationId(pubId);
    }

    @Override
    @Transactional
    public Location newLocation(String location) {
        return locationDao.newLocation(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Location findById(Long locationId) {
        Optional<Location> location = locationDao.findById(locationId);
        if(location.isEmpty()){
            throw new LocationNotFoundException("Location not found");
        }
        return location.get();
    }
}
