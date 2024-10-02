package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.stereotype.Service;

@Service
public class PublicationServiceImpl implements PublicationService {

    private final PublicationDao pubDao;
    private final LocationService locationService;

    public PublicationServiceImpl(final PublicationDao pubDao, final LocationService locationService) {
        this.pubDao = pubDao;
        this.locationService = locationService;
    }

    @Override
    public long createPublication(long bookId, long userId, String location, PublicationState publicationState) {
        return pubDao.createPublication(bookId, userId, locationService.newLocation(location), publicationState);
    }

    @Override
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, String location, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userId, location, publicationState);
        }
    }

    @Override
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication.getPublicationId());
    }

    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        return pubDao.getPublicationByPublicationId(publicationId);
    }

    @Override
    public PaginatedResponse<Publication> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, User user, SortType sortType, int currentPage) {
        return pubDao.getFilteredSortedOrderedPublicationsByPageExcludingUser(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, user.getUserId(), sortType, currentPage);
    }
}
