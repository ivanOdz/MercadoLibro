package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;

import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

//    Book createBook(Long bookModelId, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, List<Image> imageList,
//                    User user, boolean newBook);

    Book createBook(URI bookModelUrn,URI userUrn, BookState bookState, Integer rating);


//    Book createNewBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
//                         Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
//                         Language language, int pages, int weight, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, User user);

    void exchangeOwnership(Book b1, Book b2);

    Optional<Book> getBookById(long bookId);

    PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, String state, String genre, int currentPage, URI userUrn, String sortType);

    List<Book> getAvailableBooksByUser(User user);

    void setAvailable(Book book, boolean available);

    List<GenreWrapper> getGenreWrapperList(String search, String state, URI userUrn);

    List<BookStateWrapper> getBookStateWrapperList(String serach, String genre, URI userUrn);

    Book updateBook(Long bookId, String bookState);

    void setImage(Long bookId, URI imageUrn);
}
