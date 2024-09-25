package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookCard;
import ar.edu.itba.paw.models.BookModelCard;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;

import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    //Optional<Book> getBookById(long bookId);

   // Book createBook (long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating);

    //void exchangeOwnership(long b1, long b2);

    //Book getBookByPubId(long pubId);

    //List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter);

    List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType);

}
