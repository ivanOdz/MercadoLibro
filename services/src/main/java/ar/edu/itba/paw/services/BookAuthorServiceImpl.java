package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.interfaces.services.BookAuthorService;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

    private final BookAuthorDao bookAuthorDao;

    public BookAuthorServiceImpl(BookAuthorDao bookAuthorDao) {
        this.bookAuthorDao = bookAuthorDao;
    }

    @Override
    public BookAuthor createBookAuthor(long bookId, long authorId) {
        return bookAuthorDao.createBookAuthor(bookId, authorId);
    }

    @Override
    public List<Author> getAuthorsByBookId(long bookId) {
        return bookAuthorDao.getAuthorsByBookId(bookId);
    }
}
