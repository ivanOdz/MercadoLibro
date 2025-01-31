package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.FavoritePublication;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface PublicationService {

    Publication createPublication(URI bookURN, URI userURN, URI locationURN);

    Publication createPublication(Long bookId, Long userId, Long locationId);

    void deletePublication(long publicationId);

    Optional<Publication> getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, long userId, boolean favorites);

    PaginatedResponse<Publication, ItemFilterMetadata> getFavoritePublications(User user, int currentPage);

    Publication getActivePublication(long publicationId);

    void deleteFavoritePublication(long publicationId);

    FavoritePublication getFavoritePublicationById(Long favoritePublicationId);

    FavoritePublication getFavoritePublicationFromUser(Long publicationId, Long userId);



    void terminatePublication(Publication publication);

    int getPublicationCountByUserId(long userId);

    void addLocation(Long publicationId, Long locationId, User user);

    List<Publication> getActivePublicationsByUser(User user);


    FavoritePublication likePublication(Long publicationId, URI userURN);


    List<GenreWrapper> getGenreWrapperList(String search, String state);

    List<BookStateWrapper> getBookStateWrapperList(String search, String genre);

    List<GenreWrapper> getMyGenreWrapperList(long userId, String search, String state);

    List<BookStateWrapper> getMyBookStateWrapperList(long userId, String search, String genre);


}
