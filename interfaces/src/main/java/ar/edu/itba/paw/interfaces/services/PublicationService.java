package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface PublicationService {

    Publication createPublication(long bookId, User user, long locationId);

    void createPublicationIfNeeded(boolean publish, long bookId, long userId, long locationId, PublicationState publicationState);

    void terminatePublication(Publication publication);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, String state, String genre, String sortType, int currentPage, User currentUser);

    int getPublicationCountByUserId(long userId);

    void addLocation(Long publicationId, Long locationId, User user);

    List<Publication> getActivePublicationsByUser(User user);

    //PaginatedResponse<Publication, ItemFilterMetadata> getMyPaginatedPublications(long userId, String search, String state, String genre, String sortType, int currentPage);

    void deletePublication(long userId, long publicationId);

    void likePublication(long publicationId, long userId);

    List<GenreWrapper> getGenreWrapperList(String search, String isBookStateFilterActive, String bookStateFilter);

    List<BookStateWrapper> getBookStateWrapperList(String search, String isGenreFilterActive, String genreFilter);

    List<GenreWrapper> getMyGenreWrapperList(long userId, String search, String isBookStateFilterActive, String bookStateFilter);

    List<BookStateWrapper> getMyBookStateWrapperList(long userId, String search, String isGenreFilterActive, String genreFilter);

    PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, int currentPage);

    Publication getActivePublication(User user, long publicationId);
}
