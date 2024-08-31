package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publications;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PublicationsService {

    Optional<Publications> getAllPublications();
}
