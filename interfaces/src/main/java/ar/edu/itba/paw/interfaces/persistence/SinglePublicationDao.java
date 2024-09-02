package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Publication;

public interface SinglePublicationDao {

    Publication createPublication(long bookId, long userId, String location);

}
