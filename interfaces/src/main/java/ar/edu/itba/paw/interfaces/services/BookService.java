package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BookService {

    //Optional<Book> getBookById(long bookId);

    //void exchangeOwnership(long b1, long b2);

    //Book getBookByPubId(long pubId);

    //List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter);

    //-------------------------------------------------------------------------------------------------//
    List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

    Number createBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, BookState bookState,
                    int edition, int rating, List<MultipartFile> imageFiles, Short publicationYear, boolean isHardcover, boolean isPocketEdition,
                    BookDimension dimension, Language language, int pages, int weight, int bookCoverIndex, boolean publish, User user, Long bookModelId);



    /*private final User owner;
    private final BookModel bookModel;
    private final BookState bookState;
    private final int exchangesQty;
    private final boolean available;
    private final List<Integer> images;*/


}
