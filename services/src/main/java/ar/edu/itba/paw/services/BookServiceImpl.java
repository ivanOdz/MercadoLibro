package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.persistence.BookDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book createBook(String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId) {
        return bookDao.createBook(isbn, title, author, editorial, description, genre, publicationState, edition, rating, image, userId);
    }

}
