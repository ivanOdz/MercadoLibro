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

import javax.transaction.Transactional;

@Service
public class PublicationServiceImpl implements PublicationService {

    private final PublicationDao pubDao;
//    private final LocationService locationService;
    private final BookStateService bookStateService;
    private final GenreService genreService;

    public PublicationServiceImpl(final PublicationDao pubDao, BookStateService bookStateService, GenreService genreService) {
        this.pubDao = pubDao;
        this.bookStateService = bookStateService;
        this.genreService = genreService;
    }

    @Override
    @Transactional
    public Publication createPublication(long bookId, long userId, long locationId, PublicationState publicationState) {
//        List<Location> locations = new ArrayList<>();
//        locations.add(locationService.newLocation(location));
        return pubDao.createPublication(bookId, userId, locationId, publicationState);
    }

    @Override
    @Transactional
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userId, locationId, publicationState);
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

//    public List<Publication> getPublicationsByUser(User user) {
//        return pubDao.getPublicationsByUserId(user.getUserId()) ;
//    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getMyPaginatedPublications(long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {
        PaginatedResponse<Publication, ItemFilterMetadata> response = pubDao.getMyPaginatedPublications(userId, search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);

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
    public void deletePublication(long publicationId) {
        pubDao.deletePublication(publicationId);
    }

    @Override
    public void likePublication(long publicationId, long userId) {
        pubDao.likePublication(publicationId, userId);
    }
}
