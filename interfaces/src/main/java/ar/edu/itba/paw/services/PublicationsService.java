package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publications;
import org.springframework.stereotype.Service;

@Service
public interface PublicationsService {

    Publications getAllPublications();
}
