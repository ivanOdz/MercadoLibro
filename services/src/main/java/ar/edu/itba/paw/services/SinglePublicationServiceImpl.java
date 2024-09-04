package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.*;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.services.SinglePublicationService;

import org.springframework.stereotype.Service;

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
    public Publication createPublication(String username, String mail, String isbn, String title, List<String> authors, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, String location) {
        
    	User user = userDao.createUser(username, mail);
        Book book = bookDao.createBook(isbn, title, editorial, description, genre, bookState, publicationState, edition, rating, image, user.getId());

        for (String author : authors) {
            System.out.println("autor: " + author);
            Author auth = authorDao.createAuthor(author);
            book_authorDao.createBook_Author(book.getBookId(), auth.getAuthorId());

        }
        return publicationDao.createPublication(book.getBookId(), user.getId(), location);
    }
}
