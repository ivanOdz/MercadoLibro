package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookStateService;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicationServiceImpl implements PublicationService {

    private final PublicationDao pubDao;
    private final LocationService locationService;
    private final BookStateService bookStateService;
    private final GenreService genreService;

    public PublicationServiceImpl(final PublicationDao pubDao, final LocationService locationService, BookStateService bookStateService, GenreService genreService) {
        this.pubDao = pubDao;
        this.locationService = locationService;
        this.bookStateService = bookStateService;
        this.genreService = genreService;
    }

    @Override
    public long createPublication(long bookId, long userId, String location, PublicationState publicationState) {
        Set<Location> locations = new HashSet<>();
        locations.add(locationService.newLocation(location));
        return pubDao.createPublication(bookId, userId, locations, publicationState);
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
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {
        PaginatedResponse<Publication, ItemFilterMetadata> response = pubDao.getPaginatedPublications(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);

        List<BookStateWrapper> bookStateWrapperList = pubDao.getBookStateQtyByPublication(search, isGenreFilterActive, genreFilter);
        List<GenreWrapper> genreWrapperList = pubDao.getGenreQtyByPublication(search, isBookStateFilterActive, bookStateFilter);

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookStateWrapper state : bookStateWrapperList) {
            bookStates.add(new BookStateWrapper(state.getBookState(), bookStateService.getBookStateDisplayName(state.getBookState()), state.getResultByState()));
        }

        List<GenreWrapper> genres = new ArrayList<>();
        for (GenreWrapper genre : genreWrapperList) {
            genres.add(new GenreWrapper(genre.getGenre(), genreService.getGenreDisplayName(genre.getGenre()), genre.getResultByGenre()));
        }

        response.getMetadata().setBookStateWrapperList(bookStates);
        response.getMetadata().setGenreWrapperList(genres);

        return response;
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        return pubDao.getPublicationCountByUserId(userId);
    }
}
