package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.PublicationCard;
import ar.edu.itba.paw.models.PublicationDetail;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PublicationsServiceImpl implements PublicationService {

    private final PublicationDao pubDao;

    public PublicationsServiceImpl(final PublicationDao pubDao) {
        this.pubDao = pubDao;
    }

    /*@Override
    public List<Publication> getAllPublications() {
        return pubDao.getAllPublications();
    }

    @Override
    public Optional<Publication> getPublicationById(long publicationId) {
        return pubDao.getPublicationById(publicationId);
    }

    @Override
    public List<Publication> getAllPublicationsFilteredBy(String search, int bookStateFilter, int genreFilter, long userId) {
        return pubDao.getAllPublicationsFilteredBy(search, bookStateFilter, genreFilter, userId);
    }

    @Override
    public Optional<Publication> getPublicationStateByBookId(long bookId) {
        return pubDao.getPublicationStateByBookId(bookId);
    }*/

    @Override
    public long createPublication(long bookId, long userId, long locationId, PublicationState publicationState){
        return pubDao.createPublication(bookId, userId, locationId, publicationState);
    }

    public List<PublicationCard> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType){
        return pubDao.getFilteredSortedOrderedPublicationsByPageExcludingUser(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, pageIndex, userId, sortType);
    }

    @Override
    public PublicationDetail getPublicationDetailByPublicationId(long publicationId) {
        return pubDao.getPublicationDetailByPublicationId(publicationId);
    }


}
