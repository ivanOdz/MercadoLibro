package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.PublicationCard;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicationService {

//    List<Publication> getAllPublications();
//
//    Optional<Publication> getPublicationById(long publicationId);
//
//    List<Publication> getAllPublicationsFilteredBy(String search, int bookStateFilter, int genreFilter,long userId);
//
//    Optional<Publication> getPublicationStateByBookId(long bookId);

    long createPublication(long bookId, long userId, long locationId, PublicationState publicationState);

    void createPublicationIfNeeded(boolean publish, long bookId, long userId, String location, PublicationState publicationState);

    List<Publication> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

    Publication getPublicationByPublicationId(long publicationId);

}
