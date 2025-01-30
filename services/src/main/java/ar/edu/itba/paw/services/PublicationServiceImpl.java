package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.LocationNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.*;


@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationDao pubDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LocationService locationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationServiceImpl.class);

    @Override
    @Transactional
    public Publication createPublication(long bookId, User user, long locationId) {
        Optional<Book> book = bookService.getBookById(bookId);
        Location location = locationService.findById(locationId);
        List<Location> locations = new ArrayList<>();
        locations.add(location);

        Publication publication = pubDao.createPublication(book.get(), user, locations, PublicationState.CURRENT);
        // In case create publication fails, this log wont appear as it will throw an exception.
        LOGGER.info("Publication of id {} successfully created", publication.getPublicationId());

        return publication;
    }

    @Override
    @Transactional
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userService.findById(userId), locationId);
        }
    }

    @Override
    @Transactional
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication);
        LOGGER.info("Publication of id {} successfully terminated", publication.getPublicationId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Publication> getPublicationByPublicationId(long publicationId) {
        return pubDao.getPublicationByPublicationId(publicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, long userId) {

        User currentUser = userService.findById(userId);
        BookState state_filter = DEFAULT_PUBLICATION_STATE_FILTER;
        if (state != null) {
            state_filter = BookState.fromString(state);
        }

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genre != null){
            genre_filter = Genre.fromString(genre);
        }

        return pubDao.getPaginatedPublications(search, state_filter, genre_filter, sortType, currentPage, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public int getPublicationCountByUserId(long userId) {
        return pubDao.getPublicationCountByUserId(userId);
    }

    @Override
    @Transactional
    public void addLocation(Long publicationId, Long locationId, User user) {
        Location location = locationService.findById(locationId);
        Optional<Publication> publication = getPublicationByPublicationId(publicationId);

        if (publication.isEmpty()) {
        	return; // TODO: deberia devolver un boleano
        }
        else if (!Objects.equals(publication.get().getUser().getUserId(), user.getUserId())) {
            LOGGER.error("User with ID {} is not the owner of the publication with ID {}", user.getUserId(), publicationId);
            throw new UserNotUnauthorizedException("User is not the owner of the publication");
        }
        pubDao.addLocation(publication.get(), location);
        LOGGER.info("Location with ID {} successfully added to publication with ID {}", locationId, publicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Publication> getActivePublicationsByUser(User user) {
        return pubDao.getActivePublicationsByUser(user) ;
    }

    /*
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getMyPaginatedPublications(long userId, String search, String state, String genre, String sortType, int currentPage) {

        BookState state_filter = DEFAULT_PUBLICATION_STATE_FILTER;
        if (state != null) {
            state_filter = BookState.fromString(state);
        }

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genre != null){
            genre_filter = Genre.fromString(genre);
        }


        return pubDao.getPaginatedPublications(userId, search, state_filter, genre_filter, sortType, currentPage, null);
    }
    */

    @Override
    @Transactional
    public void deletePublication(long userId, long publicationId) {
        Optional<Publication> p = getPublicationByPublicationId(publicationId);
        if(p.isPresent() && p.get().getUser().getUserId() == userId) {
            p.get().getBook().setAvailable(true);
            pubDao.deletePublication(publicationId);
            LOGGER.info("Publication with ID {} deleted successfully, Book marked as available", publicationId);
        }
    }

    @Override
    @Transactional
    public void likePublication(long publicationId, long userId) {
        if(getPublicationByPublicationId(publicationId) != null) {
            pubDao.likePublication(publicationId, userId);
            LOGGER.info("User with ID {} liked Publication with ID {}", userId, publicationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search, String state) {
        boolean bookStateFilterActive = state != null;

        BookState state_filter = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state_filter = BookState.fromString(state);
            if (state_filter == null) {
                bookStateFilterActive = false;
            }
        }

        return pubDao.getGenreQtyByPublication(null, search, state_filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getMyGenreWrapperList(long userId, String search, String state) {
        boolean bookStateFilterActive = state != null;

        BookState state_filter = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state_filter = BookState.fromString(state);
            if (state_filter == null) {
                bookStateFilterActive = false;
            }
        }

        return pubDao.getGenreQtyByPublication(userId, search, state_filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(String search, String genre) {
        boolean genreFilterActive = genre != null;

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre_filter = Genre.fromString(genre);
            if(genre_filter == null){
                genreFilterActive = false;
            }
        }

        return pubDao.getBookStateQtyByPublication(null, search, genre_filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getMyBookStateWrapperList(long userId, String search, String genre) {
        boolean genreFilterActive = genre != null;

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre_filter = Genre.fromString(genre);
            if(genre_filter == null){
                genreFilterActive = false;
            }
        }

        return pubDao.getBookStateQtyByPublication(userId, search, genre_filter);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, int currentPage) {
        return pubDao.getFavoritePublications(user, currentPage);
    }

    @Override
    public Optional<Publication> getActivePublication(User user, long publicationId) {
        return pubDao.getActivePublicationById(user, publicationId);
    }
}
