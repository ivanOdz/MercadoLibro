package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.*;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.services.SinglePublicationService;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SinglePublicationServiceImpl implements SinglePublicationService {

    private final SinglePublicationDao publicationDao;
    private final UserDao userDao;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final BookAuthorDao book_authorDao;

    public SinglePublicationServiceImpl(SinglePublicationDao publicationDao, UserDao userDao, BookDao bookDao, AuthorDao authorDao, BookAuthorDao bookAuthorDao) {
        this.publicationDao = publicationDao;
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.book_authorDao = bookAuthorDao;
    }

    @Override
    public Publication createPublication(int bookModelId, int ownerId, BookState bookState, int exchangesQty, int rating) {
        
        Book book = bookDao.createBook(bookModelId, ownerId, bookState, exchangesQty, rating);

        // TODO: Obtener lista de autores dado un libro
        // Dado el bookModelId, tomar el authorId de la tabla book_author

        //List<Author> authors =
        /*for (String author : authors) {
            Author auth = authorDao.createAuthor(author);
            book_authorDao.createBookAuthor(book.getBookId(), auth.getAuthorId());

        }*/
        //return publicationDao.createPublication(book.getBookId(), user.getId(), location);
        return new Publication(1,1, BookState.NEW.getValue(), PublicationState.CURRENT, new Timestamp(new Date().getTime()), 1);
    }
}
