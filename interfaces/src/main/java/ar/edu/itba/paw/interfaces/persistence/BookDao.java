package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book createBook(int bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating);

    Optional<Book> getBookById(long bookId);

    void exchangeOwnership(long b1, long b2);

    Book getBookByPubId(long pubId);

    List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search);
}
