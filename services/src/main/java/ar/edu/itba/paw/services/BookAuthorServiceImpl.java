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

    private final BookAuthorDao book_authorDao;

    public BookAuthorServiceImpl(BookAuthorDao bookAuthorDao) {
        book_authorDao = bookAuthorDao;
    }

    @Override
    public BookAuthor createBook_Author(long bookId, long authorId, List<BookAuthor> bookAuthors) {
        return book_authorDao.createBook_Author(bookId, authorId);
    }

    @Override
    public List<Author> getAuthorsByBookId(long bookId) {
        return book_authorDao.getAuthorsByBookId(bookId);
    }
}
