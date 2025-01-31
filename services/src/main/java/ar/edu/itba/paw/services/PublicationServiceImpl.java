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
import ar.edu.itba.paw.utils.UrnResolverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
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
    // TODO: Parsear las URI´s para obtener el ID.
    public Publication createPublication(URI bookURN, URI userURN, URI locationURN) {
        return createPublication(UrnResolverUtil.getBookId(bookURN), UrnResolverUtil.getUserId(userURN), UrnResolverUtil.getLocationId(locationURN));
    }

    @Override
    public Publication createPublication(Long bookId, Long userId, Long locationId) {
        Book book = bookService.getBookById(bookId).orElseThrow(() -> new BookBadRequest("Invalid book urn"));
        User user = userService.findById(userId);
        Location location = locationService.findById(locationId);

        List<Location> locations = new ArrayList<>();
        locations.add(location);

        Publication publication = pubDao.createPublication(book, user, locations, PublicationState.CURRENT);
        // In case create publication fails, this log wont appear as it will throw an exception.
        LOGGER.info("Publication of id {} successfully created", publication.getPublicationId());

        return publication;
    }


    // Hacer funcion que reciba URN y que extraiga el id usando el uri resolver.


    /*@Override
    @Transactional
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userService.findById(userId), locationId);
        }
    }*/

    @Override
    @Transactional
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication);
        LOGGER.info("Publication of id {} successfully terminated", publication.getPublicationId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Publication> getPublicationByPublicationId(long publicationId) {
        Optional<Publication> publication = pubDao.getPublicationByPublicationId(publicationId);
        if (publication.isEmpty()) {
            LOGGER.warn("Publication of id {} not found", publicationId);
            throw new PublicationNotFoundException("Publication not found");
        }
        return publication;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, long userId, boolean favorites) {

        User currentUser = userService.findById(userId);

        BookState state_filter = BookState.fromString(state);
        state_filter = state_filter == null ? DEFAULT_PUBLICATION_STATE_FILTER : state_filter;

        Genre genre_filter = Genre.fromString(genre);
        genre_filter = genre_filter == null ? DEFAULT_PUBLICATION_GENRE_FILTER : genre_filter;

        if(favorites && currentUser == null){
            throw new UserMissingBadRequest("UserId is required when filtering favorites");
        }

        return favorites ? pubDao.getFavoritePublications(search, state_filter, genre_filter, sortType, currentPage, currentUser) :
                pubDao.getPaginatedPublications(search, state_filter, genre_filter, sortType, currentPage, currentUser);
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

        if (!Objects.equals(publication.get().getUser().getUserId(), user.getUserId())) {
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
    public FavoritePublication likePublication(Long publicationId, URI userURN) {
        Publication publication = pubDao.getPublicationByPublicationId(publicationId).
                orElseThrow(() -> new PublicationBadRequestException("Invalid Publication URN"));
        User user = userService.findById(UrnResolverUtil.getUserId(userURN));

        FavoritePublication fp = pubDao.markFavoritePublication(publicationId, user.getUserId());
        LOGGER.info("User with ID {} liked Publication with ID {}", publicationId, user.getUserId());
        return fp;
    }

    @Override
    public void deleteFavoritePublication(long publicationId) {
        pubDao.unmarkFavoritePublication(publicationId);
    }

    @Override
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
    public FavoritePublication getFavoritePublicationFromUser(Long publicationId, Long userId) {
        Publication publication = pubDao.getPublicationByPublicationId(publicationId)
                .orElseThrow(() -> new PublicationBadRequestException("Invalid publication id"));

        User user = userService.findById(userId);

        return pubDao.getFavoritePublicationFromUser(publication.getPublicationId(), user.getUserId())
                .orElseThrow(()-> new FavoritePublicationNotFoundException("No favorite publication found"));
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

    // TODO: Hacer filtros de esto
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getFavoritePublications(User user, int currentPage) {
        //return pubDao.getFavoritePublications(user.getUserId(), currentPage);
        return new PaginatedResponse<>(null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Publication getActivePublication(long publicationId) {
        Optional<Publication> publication = getPublicationByPublicationId(publicationId);
        if(publication.get().getPublicationState().equals(PublicationState.TERMINATED)) {
            throw new PublicationNotFoundException("Publication with ID " + publicationId + " not found");
        }
        return publication.get();
    }
}
