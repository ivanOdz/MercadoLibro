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

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, BookState state, Genre genre, String sortType, int currentPage, User currentUser);

    int getPublicationCountByUserId(long userId);

    List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, Genre genre);

    List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, BookState state);

    void deletePublication(long publicationId);

    void likePublication(long publicationId, long userId);

    PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, int currentPage);

    void addLocation(Publication publication, Location location);

    List<Publication> getActivePublicationsByUser(User user);

    Publication getActivePublicationById(User user, long publicationId);
}
