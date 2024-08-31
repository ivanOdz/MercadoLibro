package ar.edu.itba.paw.services;

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
    public Optional<Publications> getAllPublications() {
        return pubDao.getAllPublications();
    }

}
