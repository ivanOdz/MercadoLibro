package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Number createBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, BookState bookState,
                      int edition, int rating, List<MultipartFile> imageFiles, Short publicationYear, boolean isHardcover, boolean isPocketEdition,
                      BookDimension dimension, Language language, int pages, int weight, int bookCoverIndex, boolean publish, User user, Long bookModelId);

    void exchangeOwnership(Book b1, Book b2);

    Optional<Book> getBookById(long bookId);

    List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

    List<Book> getAvailableBooksByUser(User user);
}
