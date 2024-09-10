package ar.edu.itba.paw.interfaces.services;

import org.springframework.stereotype.Service;

@Service
public interface LocationService {
    String getLocationByPublicationId(long pubId);

}
