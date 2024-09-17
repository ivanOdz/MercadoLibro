package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(final BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book createBook (long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating) {
        return bookDao.createBook(bookModelId, ownerId, bookState, exchangesQty, rating);
    }

    @Override
    public Optional<Book> getBookById(long publicationId) {
        return bookDao.getBookById(publicationId);
    }

    @Override
    public void exchangeOwnership(long b1, long b2) {
        bookDao.exchangeOwnership(b1, b2);
    }

    @Override
    public Book getBookByPubId(long pubId) {
        return bookDao.getBookByPubId(pubId);
    }

    @Override
    public List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter) {
        return bookDao.getAllBooksByOwnerIdAndFilteredBy(ownerId, search, bookStateFilter, genreFilter);
    }
}
