package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.PublicationCard;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;

import java.util.List;
import java.util.Optional;

public interface PublicationDao {

//    List<Publication> getAllPublications();
//
//    Optional<Publication> getPublicationById(long publicationId);
//
//    List<Publication> getAllPublicationsFilteredBy(String search, int bookStateFilter, int genreFilter, long userId);

    void terminatePublication(long pubId);

//    Optional<Publication> getPublicationStateByBookId(long bookId);

    long createPublication(long bookId, long userId, long locationId, PublicationState publicationState);

    List<PublicationCard> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

}
