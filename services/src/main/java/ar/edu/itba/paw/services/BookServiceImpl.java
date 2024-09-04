package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(final BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book createBook(String isbn, String title, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, long userId) {
        return bookDao.createBook(isbn, title, editorial, description, genre, bookState, publicationState, edition, rating, image, userId);
    }

    @Override
    public Optional<Book> getBookById(long publicationId) {
        return bookDao.getBookById(publicationId);
    }

    @Override
    public void exchangeOwnership(long b1, long b2) {
        bookDao.exchangeOwnership(b1, b2);
    }
}
