package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface PublicationService {

    Publication createPublication(long bookId, long userId, String location, PublicationState publicationState);

    void createPublicationIfNeeded(boolean publish, long bookId, long userId, String location, PublicationState publicationState);

    void terminatePublication(Publication publication);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage);

    int getPublicationCountByUserId(long userId);

//    List<Publication> getPublicationsByUser(User user);

    PaginatedResponse<Publication, ItemFilterMetadata> getMyPaginatedPublications(long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage);

    void deletePublication(long publicationId);
}
