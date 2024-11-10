package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.PublicationNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_GENRE_FILTER;
import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_STATE_FILTER;

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

    @Override
    @Transactional
    public Publication createPublication(long bookId, long userId, long locationId, PublicationState publicationState) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);
        Location location = locationService.findById(locationId);
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        return pubDao.createPublication(book, user, locations, publicationState);
    }

    @Override
    @Transactional
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userId, locationId, publicationState);
        }
    }

    @Override
    @Transactional
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication);
    }

    @Override
    @Transactional(readOnly = true)
    public Publication getPublicationByPublicationId(long publicationId) {
        Optional<Publication> publication = pubDao.getPublicationByPublicationId(publicationId);
        if (publication.isEmpty()) {
            throw new PublicationNotFoundException("Publication not found");
        }
        return publication.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String isBookStateFilterActive, String bookStateFilter, String isGenreFilterActive, String genreFilter, String sortType, String currentPage, User currentUser) {

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

        return pubDao.getPaginatedPublications(null, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage, currentUser);
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
        Publication publication = getPublicationByPublicationId(publicationId);
        if (!Objects.equals(publication.getUser().getUserId(), user.getUserId())) {
            throw new UserNotUnauthorizedException("User is not the owner of the publication");
        }
        pubDao.addLocation(publication, location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Publication> getActivePublicationsByUser(User user) {
        return pubDao.getActivePublicationsByUser(user) ;
    }

    @Override
    @Transactional(readOnly = true)
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


        return pubDao.getPaginatedPublications(userId, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage, null);
    }

    @Override
    @Transactional
    public void deletePublication(long publicationId) {
        Publication p = getPublicationByPublicationId(publicationId);
        p.getBook().setAvailable(true);
        pubDao.deletePublication(publicationId);
    }

    @Override
    @Transactional
    public void likePublication(long publicationId, long userId) {
        pubDao.likePublication(publicationId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search, String isBookStateFilterActive, String bookStateFilter) {
        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);

        BookState state = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        return pubDao.getGenreQtyByPublication(null, search, bookStateFilterActive, state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getMyGenreWrapperList(long userId, String search, String isBookStateFilterActive, String bookStateFilter) {
        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);

        BookState state = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        return pubDao.getGenreQtyByPublication(userId, search, bookStateFilterActive, state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(String search, String isGenreFilterActive, String genreFilter) {
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        return pubDao.getBookStateQtyByPublication(null,search, genreFilterActive, genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getMyBookStateWrapperList(long userId, String search, String isGenreFilterActive, String genreFilter) {
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        return pubDao.getBookStateQtyByPublication(userId, search, genreFilterActive, genre);
    }

    @Override
    public PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, String currentPage) {
        return pubDao.getFavoritePublications(user, currentPage);
    }

}
