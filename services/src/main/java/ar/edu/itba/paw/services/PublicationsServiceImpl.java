package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.Publications;
import ar.edu.itba.paw.persistence.PublicationsDao;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PublicationsServiceImpl implements PublicationsService {

    private final PublicationsDao pubDao;

    public PublicationsServiceImpl(final PublicationsDao pubDao) {
        this.pubDao = pubDao;
    }

    @Override
    public Publications getAllPublications() {
        return pubDao.getAllPublications();
    }

    public Optional<Publication> getPublicationById(long publicationId) {
        return pubDao.getPublicationById(publicationId);
    }

}
