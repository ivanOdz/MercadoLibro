package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Publications;

import java.util.Optional;

public interface PublicationsDao {

    Optional<Publications> getAllPublications();

}
