package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.models.Publication;

import java.util.List;
import java.util.Optional;


@Service
public class PublicationsServiceImpl implements PublicationService {

    private final PublicationDao pubDao;

    public PublicationsServiceImpl(final PublicationDao pubDao) {
        this.pubDao = pubDao;
    }

    @Override
    public List<Publication> getAllPublications() {
        return pubDao.getAllPublications();
    }

    @Override
    public Optional<Publication> getPublicationById(long publicationId) {
        return pubDao.getPublicationById(publicationId);
    }

    @Override
    public List<Publication> getAllPublicationsFilteredBy(String search, long userId) {
        return pubDao.getAllPublicationsFilteredBy(search, userId);
    }

    @Override
    public Optional<Publication> getPublicationStateByBookId(long bookId) {
        return pubDao.getPublicationStateByBookId(bookId);
    }

    @Override
    public void createPublication(long bookId, long userId, long locationId) {
        pubDao.createPublication(bookId, userId, locationId);
    }


}
