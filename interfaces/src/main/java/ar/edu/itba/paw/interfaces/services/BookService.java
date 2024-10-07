package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;

import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BookService {

    Number createBook(Long bookModelId, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, List<Integer> imagesId, User user, boolean newBook);


    Number createNewBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                         Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                         Language language, int pages, int weight, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, User user);

    void exchangeOwnership(Book b1, Book b2);

    Book getBookById(long bookId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType);

    List<Book> getAvailableBooksByUser(User user);
}
