package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;

import java.util.List;
import java.util.Set;

public interface PublicationDao {

    Long createPublication(long bookId, long userId, Set<Location> location, PublicationState publicationState);

    void terminatePublication(long pubId);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage);

    int getPublicationCountByUserId(long userId);

    List<BookStateWrapper> getBookStateQtyByPublication(String search, boolean isGenreFilterActive, Genre genreFilter);

    List<GenreWrapper> getGenreQtyByPublication(String search, boolean isBookStateFilterActive, BookState bookStateFilter);

}
