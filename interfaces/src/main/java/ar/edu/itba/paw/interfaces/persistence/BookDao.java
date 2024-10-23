package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface BookDao {

    Book createBook(long bookModelId, User owner, BookState bookState, List<Integer> images);

    void createBookRating(User user, BookModel bookModel, int rating);

    void createBookImage(long bookId, List<Integer> images);

    void setOwner(Book book, User user);

    Book getBookById(long bookId);

    List<Book> getAllBooksByUser(long userId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType);

    List<BookStateWrapper> getBookStateQtyByBook(String search, boolean isGenreFilterActive, Genre genreFilter, long userId);

    List<GenreWrapper> getGenreQtyByBook(String search, boolean isBookStateFilterActive, BookState bookStateFilter, long userId);


}
