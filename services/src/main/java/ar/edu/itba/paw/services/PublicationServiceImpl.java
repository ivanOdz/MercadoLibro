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

import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_GENRE_FILTER;
import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_STATE_FILTER;

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
    @Transactional
    public Publication createPublication(long bookId, long userId, String location, PublicationState publicationState) {
        List<Location> locations = new ArrayList<>();
        locations.add(locationService.newLocation(location));
        return pubDao.createPublication(bookId, userId, locations, publicationState);
    }

    @Override
    @Transactional
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
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String isBookStateFilterActive, String bookStateFilter, String isGenreFilterActive, String genreFilter, String sortType, String currentPage) {

        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        BookState state = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        PaginatedResponse<Publication, ItemFilterMetadata> response = pubDao.getPaginatedPublications(null, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage);

        List<BookStateWrapper> bookStateWrapperList = pubDao.getBookStateQtyByPublication(null,search, genreFilterActive, genre);
        List<GenreWrapper> genreWrapperList = pubDao.getGenreQtyByPublication(null, search, bookStateFilterActive, state);

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookStateWrapper bookState : bookStateWrapperList) {
            bookStates.add(new BookStateWrapper(bookState.getBookState(), bookStateService.getBookStateDisplayName(bookState.getBookState()), bookState.getResultByState()));
        }

        List<GenreWrapper> genres = new ArrayList<>();
        for (GenreWrapper genreWrapper : genreWrapperList) {
            genres.add(new GenreWrapper(genreWrapper.getGenre(), genreService.getGenreDisplayName(genreWrapper.getGenre()), genreWrapper.getResultByGenre()));
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
    public PaginatedResponse<Publication, ItemFilterMetadata> getMyPaginatedPublications(long userId, String search, String isBookStateFilterActive, String bookStateFilter, String isGenreFilterActive, String genreFilter, String sortType, String currentPage) {
        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        BookState state = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }


        PaginatedResponse<Publication, ItemFilterMetadata> response = pubDao.getPaginatedPublications(userId, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage);

        List<BookStateWrapper> bookStateWrapperList = pubDao.getBookStateQtyByPublication(userId,search, genreFilterActive, genre);
        List<GenreWrapper> genreWrapperList = pubDao.getGenreQtyByPublication(userId, search, bookStateFilterActive, state);

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookStateWrapper bookState : bookStateWrapperList) {
            bookStates.add(new BookStateWrapper(bookState.getBookState(), bookStateService.getBookStateDisplayName(bookState.getBookState()), bookState.getResultByState()));
        }

        List<GenreWrapper> genres = new ArrayList<>();
        for (GenreWrapper genreWrapper : genreWrapperList) {
            genres.add(new GenreWrapper(genreWrapper.getGenre(), genreService.getGenreDisplayName(genreWrapper.getGenre()), genreWrapper.getResultByGenre()));
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
