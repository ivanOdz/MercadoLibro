package ar.edu.itba.paw.services;

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
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    /*
    @Override
    @Transactional
    public Book createBook(Long bookModelId, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, List<Image> imageList,
                           User user, boolean newBook) {
        LOGGER.info("Starting the creation of the book with model ID: {}", bookModelId);

        List<BookImage> bookImages = new ArrayList<>();
        List<Image> images;
        
        if (!newBook) {  // If it's a new book, the images are already saved
            LOGGER.info("Saving new images for the book.");
            images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));
        } else {
            LOGGER.info("Using provided image list for the book.");
            images = imageList;
        }

        int order = 0;
        for (Image img : images) {
            BookImage bookImage = new BookImage();
            bookImage.setImage(img);
            bookImage.setImageOrder(order++);
            bookImage.setImageDatetime(Timestamp.valueOf(LocalDateTime.now()));
            bookImages.add(bookImage);
        }

        LOGGER.info("Creating book rating for user: {} with rating: {}", user.getUserId(), rating);
        bookDao.createBookRating(user, bookModelService.getBookModelByBookModelId(bookModelId), rating);

        LOGGER.info("Creating book for book model ID: {}", bookModelId);
        Book book = bookDao.createBook(bookModelService.getBookModelByBookModelId(bookModelId), user, bookState);

        // Adding images to the book
        LOGGER.info("Adding images to the newly created book.");
        
        for (BookImage bookImage : bookImages) {
            bookImage.setBook(book);
            book.getImages().add(bookImage);
        }
        bookDao.saveBookImages(bookImages);
        LOGGER.info("Successfully created book with ID: {}", book.getBookId());

        return book;
    }*/

    @Override
    @Transactional
    public Book createBook(Long bookModelId, User user, BookState bookState, Integer rating){
        LOGGER.info("Creating book for book model ID: {}", bookModelId);

        BookModel bm = bookModelService.getBookModelByBookModelId(bookModelId);
        bookDao.createBookRating(user, bm, rating);

        return bookDao.createBook(bookModelService.getBookModelByBookModelId(bookModelId), user, bookState);
    }

    /*
    @Override
    @Transactional
    public Book createNewBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                                  Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                                  Language language, int pages, int weight, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, User user){
        LOGGER.info("Starting creation of new book with ISBN: {}", isbn);

        LOGGER.info("Saving images for the new book.");
        List<Image> images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));

        BookModel bookModel = bookModelService.createBookModel(isbn, title, authors, publisher, description, genre, edition,
                publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight);


        return createBook(bookModel.getBookModelId(), bookState, rating, imageFiles, bookCoverIndex, images, user, true);
    }
    */

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
    @Transactional(readOnly = true)
    public Book getBookById(long bookId) {
    	
        LOGGER.info("Attempting to retrieve Book with ID: {}", bookId);

        Optional<Book> book = bookDao.getBookById(bookId);
        if (book.isEmpty()) {
            LOGGER.warn("Book with ID: {} not found", bookId);
            throw new BookNotFoundException("Book not found");
        }

        LOGGER.info("Book with ID: {} found successfully", bookId);
        return book.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, String state, String genre, int currentPage, long userId, String sortType) {

        boolean bookStateFilterActive = state != null;
        boolean genreFilterActive = genre != null;

        BookState state_filter = DEFAULT_BOOK_STATE_FILTER;
        if (bookStateFilterActive) {
            state_filter = BookState.fromString(state);
            if (state_filter == null) {
                bookStateFilterActive = false;
            }
        }

        Genre genre_filter = DEFAULT_BOOK_GENRE_FILTER;
        if (genreFilterActive){
            genre_filter = Genre.fromString(genre);
            if(genre_filter == null){
                genreFilterActive = false;
            }
        }

        return bookDao.getPaginatedBooks(search, state_filter, genre_filter, currentPage, userId, sortType);
    }

    private List<MultipartFile> arrangeImages(List<MultipartFile> images, int bookCoverIndex) {
    	
        if (bookCoverIndex == 0) {
            return images;
        }
        
        List<MultipartFile> toReturn = new ArrayList<>();
        toReturn.add(images.get(bookCoverIndex));
        
        for (MultipartFile image : images) {
            if (images.indexOf(image) != bookCoverIndex){
                toReturn.add(image);
            }
        }
        return toReturn;
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
    	
        boolean bookStateFilterActive = state != null;

        BookState state_filter = DEFAULT_PUBLICATION_STATE_FILTER;
        
        if (bookStateFilterActive) {
            state_filter = BookState.fromString(state);
            if (state_filter == null) {
                bookStateFilterActive = false;
            }
        }

        return bookDao.getGenreQtyByBook(search, state_filter, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(String serach, String genre, long userId) {
    	
        boolean genreFilterActive = genre != null;

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if (genreFilterActive) {
            genre_filter = Genre.fromString(genre);
            if (genre_filter == null) {
                genreFilterActive = false;
            }
        }

        return bookDao.getBookStateQtyByBook(serach, genre_filter, userId);
    }

    @Override
    @Transactional
    public Book updateBookState(Long bookId, String bookState) {
    	
        LOGGER.info("Attempting to update the state of Book with ID: {} to state: {}", bookId, bookState);

        Optional<Book> updatedBook = bookDao.updateBookState(bookId, bookState);
        if (updatedBook.isPresent()) {
            LOGGER.info("Successfully updated Book with ID: {} to state: {}", bookId, bookState);
            return updatedBook.get();
        } else {
            LOGGER.warn("Book with ID: {} not found, state update failed", bookId);
            return null;  // Return null if the book wasn't found or update failed
        }
    }

    @Transactional
    @Override
    public void setImage(Long bookId, Long imageId) {
        Image image = imageService.getImageById(imageId);
        Book book = getBookById(bookId);
        BookImage bookImage = new BookImage();
        bookImage.setImage(image);
        bookImage.setImageOrder(book.getImages().size());
        bookImage.setImageDatetime(Timestamp.valueOf(LocalDateTime.now()));
        bookDao.setImage(book, bookImage);
    }
}
