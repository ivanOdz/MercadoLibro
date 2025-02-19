package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.FavoritePublication;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface PublicationService {

    Publication createPublication(Long bookId, Long userId, Long locationId, Boolean toExchange);

    void deletePublication(long publicationId);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, Long userId, Boolean favorites, Long locationId);

    void deleteFavoritePublication(long publicationId);

    FavoritePublication getFavoritePublicationById(Long favoritePublicationId);

    FavoritePublication getFavoritePublicationFromUser(Long publicationId, Long userId);

    void updatePublication(Long publicationId, Long locationId);

    void terminatePublication(Publication publication);

    void addLocation(Long publicationId, Long locationId, User user);

    FavoritePublication likePublication(Long publicationId, Long userId);

    List<GenreWrapper> getGenreWrapperList(Long userId, String search, String state, boolean favorites);

    List<BookStateWrapper> getBookStateWrapperList(Long userId, String search, String state, boolean favorites);
}
