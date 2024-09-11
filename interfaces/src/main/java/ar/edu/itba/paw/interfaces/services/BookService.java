package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Optional<Book> getBookById(long bookId);

    Book createBook (long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating);

    void exchangeOwnership(long b1, long b2);

    Book getBookByPubId(long pubId);

    List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search);
}
