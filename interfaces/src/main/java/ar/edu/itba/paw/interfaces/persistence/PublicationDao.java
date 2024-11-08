package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface PublicationDao {

    Publication createPublication(long bookId, long userId, long locationId, PublicationState publicationState);

    void terminatePublication(long pubId);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(Long userId,String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String sortType, String currentPage, User currentUser);

    int getPublicationCountByUserId(long userId);

    List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter);

    List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter);

    void deletePublication(long publicationId);

    void likePublication(long publicationId, long userId);

    PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(Long userId, String currentPage);
}
