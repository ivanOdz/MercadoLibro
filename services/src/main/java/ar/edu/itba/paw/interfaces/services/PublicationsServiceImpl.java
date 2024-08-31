package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publications;
import ar.edu.itba.paw.persistence.PublicationsDao;
import org.springframework.stereotype.Service;


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
}
