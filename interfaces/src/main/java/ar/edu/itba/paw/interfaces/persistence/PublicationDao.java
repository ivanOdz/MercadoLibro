package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;
import java.util.Optional;

public interface PublicationDao {

    List<Publication> getAllPublications();

    Optional<Publication> getPublicationById(long publicationId);

    List<Publication> getAllPublicationsFilteredBy(String search, long userId);


    void terminatePublication(long pubId);

    Optional<Publication> getPublicationStateByBookId(long bookId);

}
