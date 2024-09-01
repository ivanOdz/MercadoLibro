package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.Publications;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PublicationsService {

    Publications getAllPublications();

    Optional<Publication> getPublicationById(long publicationId);
}
