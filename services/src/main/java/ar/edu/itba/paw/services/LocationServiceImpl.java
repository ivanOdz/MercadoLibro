package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.LocationNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.models.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Override
    @Transactional
    public Location newLocation(String location) {
        LOGGER.info("Creating new location: {}", location);

        Location newLocation = locationDao.newLocation(location);

        if (newLocation == null) {
            LOGGER.warn("Failed to create new location: {}", location);
        } else {
            LOGGER.info("New location created successfully: {}", location);
        }

        return newLocation;
    }

    @Override
    @Transactional(readOnly = true)
    public Location findById(Long locationId) {
        LOGGER.info("Searching for location with ID: {}", locationId);

        Optional<Location> location = locationDao.findById(locationId);

        if (location.isEmpty()) {
            LOGGER.warn("Location with ID: {} not found", locationId);
            throw new LocationNotFoundException("Location not found");
        }

        LOGGER.info("Location with ID: {} found", locationId);
        return location.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getLocationByPublicationId(Long publicationId) {
        LOGGER.info("Searching for locations of publication of ID: {}", publicationId);

        return locationDao.getLocationByPublicationId(publicationId).stream().toList();
    }


}
