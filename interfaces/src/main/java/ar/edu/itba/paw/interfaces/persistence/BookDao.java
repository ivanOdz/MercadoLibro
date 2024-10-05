package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.SortType;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Number createBook(long bookModelId, User owner, BookState bookState, List<Integer> images);

    void createBookRating(User user, long bookModelId, int rating);

    void createBookImage(long bookId, List<Integer> images);

    void setOwner(long bookId, long userId);

    Optional<Book> getBookById(long bookId);

    List<Book> getAllBooksByUser(long userId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType);
}
