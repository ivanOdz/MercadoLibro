package ar.edu.itba.paw.interfaces.services;

public interface LocationService {
    String getLocationByPublicationId(long pubId);

    long newLocation(String location);

}
