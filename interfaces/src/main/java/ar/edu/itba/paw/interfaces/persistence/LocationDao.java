package ar.edu.itba.paw.interfaces.persistence;

public interface LocationDao {

    String getLocationByPublicationId(long pubId);

    long newLocation(String location);
}
