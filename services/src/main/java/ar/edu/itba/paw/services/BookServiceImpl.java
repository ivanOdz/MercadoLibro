package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.utils.Constants.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BookModelService bookModelService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Override
    @Transactional
    public Book createBook(Long bookModelId, Long userId, BookState bookState, Integer rating, List<Long> imageIds){
        LOGGER.info("Creating book for book model ID: {}", bookModelId);

        User u = userService.findById(userId);

        BookModel maybeBm = bookModelService.getBookModelByBookModelId(bookModelId);

        bookDao.createBookRating(u, maybeBm, rating);

        Book book =  bookDao.createBook(maybeBm, u, bookState);

        for (Long imgId : imageIds) {
            setImage(book, imgId);
        }

        return book;
    }

    @Override
    @Transactional
    public void exchangeOwnership(Book b1, Book b2) {
        LOGGER.info("Exchanging ownership of books: {} and {}", b1.getBookId(), b2.getBookId());

        User owner1 = b1.getOwner();
        User owner2 = b2.getOwner();

        LOGGER.info("Current owners - Book {}: {}, Book {}: {}", b1.getBookId(), owner1.getUserId(), b2.getBookId(), owner2.getUserId());

        bookDao.setOwner(b1, owner2);
        setAvailable(b1, true);
        LOGGER.info("Ownership of Book {} transferred to User {}", b1.getBookId(), owner2.getUserId());

        bookDao.setOwner(b2, owner1);
        setAvailable(b2, true);
        LOGGER.info("Ownership of Book {} transferred to User {}", b2.getBookId(), owner1.getUserId());
    }

    @Override
    @Transactional
    public Book getBookById(long bookId) {
    	
        LOGGER.info("Attempting to retrieve Book with ID: {}", bookId);

        Optional<Book> b =  bookDao.getBookById(bookId);

        if (b.isEmpty()) {
            LOGGER.warn("Book with ID: {} not found", bookId);
            throw new BookNotFoundException("Book not found");
        }
        return b.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, String state, String genre, int currentPage, long userId, String sortType) {
        BookState stateFilter = BookState.fromString(state);
        Genre genreFilter = Genre.fromString(genre);

        return bookDao.getPaginatedBooks(search, stateFilter, genreFilter, currentPage, userId, sortType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooksByUser(User user){
        if(user == null){
            return Collections.emptyList();
        }
        return bookDao.getAllBooksByUser(user.getUserId()).stream().filter(Book::isAvailable).toList();
    }

    @Override
    @Transactional
    public void setAvailable(Book book, boolean available) {
        bookDao.setAvailable(book, available);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search, String state, long userId) {
        BookState state_filter = BookState.fromString(state);

        return bookDao.getGenreQtyByBook(search, state_filter, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(String serach, String genre, long userId) {
        Genre genre_filter = Genre.fromString(genre);

        return bookDao.getBookStateQtyByBook(serach, genre_filter, userId);
    }

    @Override
    @Transactional
    public Book updateBook(Long bookId, String bookState) {
        LOGGER.info("Attempting to update the state of Book with ID: {} to state: {}", bookId, bookState);

        Optional<Book> updatedBook = bookDao.updateBookState(bookId, bookState);
        if (updatedBook.isPresent()) {
            LOGGER.info("Successfully updated Book with ID: {} to state: {}", bookId, bookState);
            return updatedBook.get();
        } else {
            LOGGER.warn("Book with ID: {} not found, state update failed", bookId);
            throw new BookNotFoundException("Book not found");
        }
    }

    private void setImage(Book book, Long imageId) {
        Image image = imageService.getImageById(imageId);

        BookImage bookImage = new BookImage();
        bookImage.setImage(image);

        // IMPLEMENT: set image order
        bookImage.setImageOrder(book.getImages().size());
        bookImage.setImageDatetime(Timestamp.valueOf(LocalDateTime.now()));
        bookDao.setImage(book, bookImage);

    }
}
