package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PublicationDao {

    Publication createPublication(Book book, User user, Location location, PublicationState publicationState);

    void terminatePublication(Publication publication);

    Optional<Publication> getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(Long userId,String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String sortType, String currentPage);

    int getPublicationCountByUserId(long userId);

    List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter);

    List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter);

    void deletePublication(long publicationId);

    void likePublication(long publicationId, long userId);

}
