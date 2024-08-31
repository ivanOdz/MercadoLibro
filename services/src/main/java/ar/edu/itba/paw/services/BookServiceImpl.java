package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.persistence.BookDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    public BookServiceImpl(final BookDao bookDao) {
        this.bookDao = bookDao;
    }

    private final BookDao bookDao;


    @Override
    public Optional<Book> getBookById(long publicationId) {
        return bookDao.getBookById(publicationId);
    }
}
