package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;

public interface BookDao {

    Book createBook(BookModel bookModel, User owner, BookState bookState);

    void createBookRating(User user, BookModel bookModel, int rating);

    void createBookImage(Book book, List<Image> images);

    void setOwner(Book book, User user);

    Book getBookById(long bookId);

    List<Book> getAllBooksByUser(long userId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String currentPage, long userId, String sortType);

    List<BookStateWrapper> getBookStateQtyByBook(String search, boolean isGenreFilterActive, Genre genreFilter, Long userId);

    List<GenreWrapper> getGenreQtyByBook(String search, boolean isBookStateFilterActive, BookState bookStateFilter, Long userId);

    void setAvailable(Book book, boolean available);

    void saveBookImages(List<BookImage> bookImages);


}
