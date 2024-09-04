package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookAuthorDao;
import ar.edu.itba.paw.interfaces.services.BookAuthorService;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.stereotype.Service;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

    private final BookAuthorDao book_authorDao;

    public BookAuthorServiceImpl(BookAuthorDao bookAuthorDao) {
        book_authorDao = bookAuthorDao;
    }

    @Override
    public BookAuthor createBook_Author(long bookId, long authorId) {
        return book_authorDao.createBook_Author(bookId, authorId);
    }
}
