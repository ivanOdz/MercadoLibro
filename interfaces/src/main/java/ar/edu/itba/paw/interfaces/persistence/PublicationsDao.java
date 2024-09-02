package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.Publications;

import java.util.Optional;

public interface PublicationsDao {

    Publications getAllPublications();

    Optional<Publication> getPublicationById(long publicationId);

    Publications getAllPublicationsFilteredBy(String search);

}
