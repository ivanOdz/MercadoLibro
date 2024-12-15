package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;
import java.util.Optional;

public interface PublicationDao {

	Publication createPublication(Book book, User user, List<Location> locations, PublicationState publicationState);

    void terminatePublication(Publication publication);

    Optional<Publication> getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(Long userId,String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String sortType, String currentPage, User currentUser);

    int getPublicationCountByUserId(long userId);

    List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter);

    List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter);

    void deletePublication(long publicationId);

    void likePublication(long publicationId, long userId);

    PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, String currentPage);

    void addLocation(Publication publication, Location location);

    List<Publication> getActivePublicationsByUser(User user);

    Publication getActivePublicationById(User user, long publicationId);
}
