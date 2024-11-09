package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book createBook(BookModel bookModel, User owner, BookState bookState);

    void createBookRating(User user, BookModel bookModel, int rating);

    void setOwner(Book book, User user);

    Optional<Book> getBookById(long bookId);

    List<Book> getAllBooksByUser(long userId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String currentPage, long userId, String sortType);

    List<BookStateWrapper> getBookStateQtyByBook(String search, boolean isGenreFilterActive, Genre genreFilter, Long userId);

    List<GenreWrapper> getGenreQtyByBook(String search, boolean isBookStateFilterActive, BookState bookStateFilter, Long userId);

    void setAvailable(Book book, boolean available);

    void saveBookImages(List<BookImage> bookImages);

    Optional<Book> updateBookState(Long bookId, String bookState);
}
