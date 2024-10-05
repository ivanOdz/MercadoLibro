package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;

public interface PublicationDao {

    long createPublication(long bookId, long userId, long locationId, PublicationState publicationState);

    void terminatePublication(long pubId);

    Publication getPublicationByPublicationId(long publicationId);

    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage);
}
