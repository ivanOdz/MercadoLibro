package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;

import java.util.List;
import java.util.Optional;

public interface BookDao {


    void setOwner(long bookId, long userId);


    //Optional<Book> getBookById(long bookId);

    //Book getBookByPubId(long pubId);

    //List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter);

    void createBookImage(long bookId, List<Integer> images);

    List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

    Number createBook(long bookModelId, User owner, BookState bookState, List<Integer> images);

    void createBookRating(User user, long bookModelId, int rating);

}
