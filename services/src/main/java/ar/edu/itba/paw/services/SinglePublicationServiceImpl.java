package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.BookDao;
import ar.edu.itba.paw.persistence.SinglePublicationDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SinglePublicationServiceImpl implements SinglePublicationService {

    private final SinglePublicationDao publicationDao;
    private final UserDao userDao;
    private final BookDao bookDao;

    public SinglePublicationServiceImpl(SinglePublicationDao publicationDao, UserDao userDao, BookDao bookDao) {
        this.publicationDao = publicationDao;
        this.userDao = userDao;
        this.bookDao = bookDao;
    }

    @Override
    public Publication createPublication(String username, String mail, String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, String location) {
        User user = userDao.createUser(username, mail);

        Book book = bookDao.createBook(isbn, title, author, editorial, description, genre, publicationState, edition, rating, image, user.getId());

        return publicationDao.createPublication(book.getBookId(), user.getId(), location);
    }
}
