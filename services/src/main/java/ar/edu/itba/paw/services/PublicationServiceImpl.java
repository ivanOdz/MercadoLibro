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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {
        PaginatedResponse<Publication, ItemFilterMetadata> response = pubDao.getPaginatedPublications(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);

        List<BookStateWrapper> bookStateWrapperList = pubDao.getBookStateQtyByPublication(search, isGenreFilterActive, genreFilter);
        List<GenreWrapper> genreWrapperList = pubDao.getGenreQtyByPublication(search, isBookStateFilterActive, bookStateFilter);
        Map<BookState, Integer> resultByStateMap = bookStateWrapperList.stream()
                .collect(Collectors.toMap(BookStateWrapper::getBookState, BookStateWrapper::getResultByState));

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookState state : BookState.values()) {
            bookStates.add(new BookStateWrapper(state, bookStateService.getBookStateDisplayName(state), resultByStateMap.getOrDefault(state, 0)));
        }

        Map<Genre, Integer> genreByStateMap = genreWrapperList.stream()
                .collect(Collectors.toMap(GenreWrapper::getGenre, GenreWrapper::getResultByGenre));

        List<GenreWrapper> genres = new ArrayList<>();
        for (Genre genre : Genre.values()) {
            genres.add(new GenreWrapper(genre, genreService.getGenreDisplayName(genre), genreByStateMap.getOrDefault(genre, 0)));
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
