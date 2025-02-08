package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.FavoritePublication;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface PublicationService {

    Publication createPublication(Long bookId, Long userId, Long locationId);

    void deletePublication(long publicationId);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, Long userId, Boolean favorites, Long locationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getFavoritePublications(User user, int currentPage);

    Publication getActivePublication(long publicationId);

    void deleteFavoritePublication(long publicationId);

    FavoritePublication getFavoritePublicationById(Long favoritePublicationId);

    FavoritePublication getFavoritePublicationFromUser(Long publicationId, Long userId);



    void terminatePublication(Publication publication);

    int getPublicationCountByUserId(long userId);

    void addLocation(Long publicationId, Long locationId, User user);

    List<Publication> getActivePublicationsByUser(User user);


    FavoritePublication likePublication(Long publicationId, Long userId);


    List<GenreWrapper> getGenreWrapperList(String search, String state);

    List<BookStateWrapper> getBookStateWrapperList(String search, String genre);

    List<GenreWrapper> getMyGenreWrapperList(Long userId, String search, String state);

    List<BookStateWrapper> getMyBookStateWrapperList(long userId, String search, String genre);


}
