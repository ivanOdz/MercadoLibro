package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_GENRE_FILTER;
import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_STATE_FILTER;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationDao pubDao;
//    private final LocationService locationService;

    @Transactional
    @Override
    public Publication createPublication(long bookId, long userId, long locationId, PublicationState publicationState) {
//        List<Location> locations = new ArrayList<>();
//        locations.add(locationService.newLocation(location));
        return pubDao.createPublication(bookId, userId, locationId, publicationState);
    }

    @Transactional
    @Override
    public void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState) {
        if (publish) {
            createPublication(bookId, userId, locationId, publicationState);
        }
    }

    @Transactional
    @Override
    public void terminatePublication(Publication publication) {
        pubDao.terminatePublication(publication.getPublicationId());
    }

    @Transactional(readOnly = true)
    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        return pubDao.getPublicationByPublicationId(publicationId);
    }

    @Transactional(readOnly = true)
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

        return pubDao.getPaginatedPublications(null, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage);
    }

    @Transactional(readOnly = true)
    @Override
    public int getPublicationCountByUserId(long userId) {
        return pubDao.getPublicationCountByUserId(userId);
    }

//    public List<Publication> getPublicationsByUser(User user) {
//        return pubDao.getPublicationsByUserId(user.getUserId()) ;
//    }

    @Transactional(readOnly = true)
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


        return pubDao.getPaginatedPublications(userId, search, bookStateFilterActive, state, genreFilterActive, genre, sortType, currentPage);
    }

    @Transactional
    @Override
    public void deletePublication(long publicationId) {
        pubDao.deletePublication(publicationId);
    }

    @Transactional
    @Override
    public void likePublication(long publicationId, long userId) {
        pubDao.likePublication(publicationId, userId);
    }

    @Transactional(readOnly = true)
    @Override
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

    @Transactional(readOnly = true)
    @Override
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

    @Transactional(readOnly = true)
    @Override
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

    @Transactional(readOnly = true)
    @Override
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


}
