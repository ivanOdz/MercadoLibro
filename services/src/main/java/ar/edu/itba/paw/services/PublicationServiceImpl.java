package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.FavoritePublicationNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.*;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
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
    public Publication createPublication(Long bookId, Long userId, Long locationId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);
        Location location = locationService.findById(locationId);

        List<Location> locations = new ArrayList<>();
        locations.add(location);

        Publication publication = pubDao.createPublication(book, user, locations, PublicationState.CURRENT);
        // In case create publication fails, this log wont appear as it will throw an exception.
        LOGGER.info("Publication of id {} successfully created", publication.getPublicationId());

        return publication;
    }


    @Override
    @Transactional
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication);
        LOGGER.info("Publication of id {} successfully terminated", publication.getPublicationId());
    }

    @Override
    @Transactional(readOnly = true)
    public Publication getPublicationByPublicationId(long publicationId) {
        Optional<Publication> publication = pubDao.getPublicationByPublicationId(publicationId);
        if (publication.isEmpty()) {
            LOGGER.warn("Publication of id {} not found", publicationId);
            throw new PublicationNotFoundException("Publication not found");
        }
        return publication.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, Long userId, Boolean favorites, Long locationId) {
        User currentUser = null;
        if(userId != null) {
            currentUser = userService.findById(userId);
        }

        BookState state_filter = BookState.fromString(state);
        Genre genre_filter = Genre.fromString(genre);

        if(favorites && currentUser == null){
            throw new UserMissingBadRequest("UserId is required when filtering favorites");
        }

        return favorites ? pubDao.getFavoritePublications(search, state_filter, genre_filter, currentPage, currentUser, locationId) :
                pubDao.getPaginatedPublications(search, state_filter, genre_filter, sortType, currentPage, currentUser, locationId);
    }

    @Override
    @Transactional
    public void addLocation(Long publicationId, Long locationId, User user) {
        Location location = locationService.findById(locationId);
        Publication publication = getPublicationByPublicationId(publicationId);

        if (!Objects.equals(publication.getUser().getUserId(), user.getUserId())) {
            LOGGER.error("User with ID {} is not the owner of the publication with ID {}", user.getUserId(), publicationId);
            throw new UserNotUnauthorizedException("User is not the owner of the publication");
        }
        pubDao.addLocation(publication, location);
        LOGGER.info("Location with ID {} successfully added to publication with ID {}", locationId, publicationId);
    }

    @Override
    @Transactional
    public void deletePublication(long publicationId) {
        Optional<Publication> maybePublication = pubDao.getPublicationByPublicationId(publicationId);

        if(maybePublication.isPresent()){
            maybePublication.get().getBook().setAvailable(true);
            pubDao.deletePublication(publicationId);
            LOGGER.info("Publication with ID {} deleted successfully, Book marked as available", publicationId);
        }
    }

    @Override
    @Transactional
    public FavoritePublication likePublication(Long publicationId, Long userId) {
        Publication publication = pubDao.getPublicationByPublicationId(publicationId).
                orElseThrow(() -> new PublicationBadRequestException("Invalid Publication URN"));
        User user = userService.findById(userId);

        FavoritePublication fp = pubDao.markFavoritePublication(publicationId, user.getUserId());
        LOGGER.info("User with ID {} liked Publication with ID {}", publicationId, user.getUserId());
        return fp;
    }

    @Override
    @Transactional
    public void deleteFavoritePublication(long publicationId) {
        pubDao.unmarkFavoritePublication(publicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritePublication getFavoritePublicationById(Long favoritePublicationId) {
        if(favoritePublicationId == null){
            throw new PublicationBadRequestException("Publication id must not be null");
        }

        Optional<FavoritePublication> maybeFp = pubDao.getFavoritePublicationById(favoritePublicationId);

        if(maybeFp.isEmpty()){
            LOGGER.warn("Favorite publication of id {} not found", favoritePublicationId);
            throw new FavoritePublicationNotFoundException("Favorite publication not found");
        }

        return maybeFp.get();
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritePublication getFavoritePublicationFromUser(Long publicationId, Long userId) {
        Publication publication = pubDao.getPublicationByPublicationId(publicationId)
                .orElseThrow(() -> new PublicationBadRequestException("Invalid publication id"));

        User user = userService.findById(userId);

        return pubDao.getFavoritePublicationFromUser(publication.getPublicationId(), user.getUserId())
                .orElseThrow(()-> new FavoritePublicationNotFoundException("No favorite publication found"));
    }

    @Override
    @Transactional
    public void updatePublication(Long publicationId, Long locationId) {
        locationService.findById(locationId);
        pubDao.updatePublication(publicationId, locationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(Long userId, String search, String state, boolean favorites) {
        BookState stateFilter = BookState.fromString(state);

        return favorites ? pubDao.getGenreQtyByFavoritePublication(userId, search, stateFilter) :
                pubDao.getGenreQtyByPublication(userId, search, stateFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(Long userId, String search, String genre, boolean favorites) {
        Genre genreFilter = Genre.fromString(genre);

        return favorites ? pubDao.getBookStateQtyByFavoritePublication(userId, search, genreFilter) :
                pubDao.getBookStateQtyByPublication(userId, search, genreFilter);
    }
}
