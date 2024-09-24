package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book createBook(long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating);

    Optional<Book> getBookById(long bookId);

    void exchangeOwnership(long b1, long b2);

    Book getBookByPubId(long pubId);

    List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter);

    List<BookCard> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);


}
