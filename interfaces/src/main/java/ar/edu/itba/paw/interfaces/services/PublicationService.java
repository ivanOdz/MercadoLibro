package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PublicationService {

    List<Publication> getAllPublications();

    Optional<Publication> getPublicationById(long publicationId);

    List<Publication> getAllPublicationsFilteredBy(String search, long userId);

    Optional<Publication> getPublicationStateByBookId(long bookId);

}
