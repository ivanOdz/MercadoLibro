package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    @Transactional
    public Book createBook(Long bookModelId, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, List<Image> imageList,
                           User user, boolean newBook) {
        List<BookImage> bookImages = new ArrayList<>();
        List<Image> images;
        if (!newBook) {  // If it's a new book, the images are already saved
            images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));
        } else {
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

        bookDao.createBookRating(user, bookModelService.getBookModelByBookModelId(bookModelId), rating);
        Book book = bookDao.createBook(bookModelService.getBookModelByBookModelId(bookModelId), user, bookState);
        for (BookImage bookImage : bookImages) {
            bookImage.setBook(book);
            book.getImages().add(bookImage);
        }
        bookDao.saveBookImages(bookImages);

        return book;
    }

    @Override
    @Transactional
    public Book createNewBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                                  Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                                  Language language, int pages, int weight, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, User user){
        List<Image> images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));

        BookModel bookModel = bookModelService.createBookModel(isbn, title, authors, publisher, description, genre, edition,
                publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, images.get(bookCoverIndex));

        return createBook(bookModel.getBookModelId(), bookState, rating, imageFiles, bookCoverIndex, images, user, true);
    }

    @Override
    @Transactional
    public void exchangeOwnership(Book b1, Book b2) {
        User owner1 = b1.getOwner();
        User owner2 = b2.getOwner();
        bookDao.setOwner(b1, owner2);
        setAvailable(b1, true);
        bookDao.setOwner(b2, owner1);
        setAvailable(b2, true);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(long bookId) {
        Optional<Book> book = bookDao.getBookById(bookId);
        if (book.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }
        return book.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, String isBookStateFilterActive, String bookStateFilter, String isGenreFilterActive, String genreFilter, String currentPage, long userId, String sortType) {

        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        BookState state = DEFAULT_BOOK_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        Genre genre = DEFAULT_BOOK_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        return bookDao.getPaginatedBooks(search, bookStateFilterActive, state, genreFilterActive, genre, currentPage, userId, sortType);
    }

    private List<MultipartFile> arrangeImages(List<MultipartFile> images, int bookCoverIndex) {
        if(bookCoverIndex == 0){
            return images;
        }
        List<MultipartFile> toReturn = new ArrayList<>();
        toReturn.add(images.get(bookCoverIndex));
        for (MultipartFile image : images) {
            if(images.indexOf(image) != bookCoverIndex){
                toReturn.add(image);
            }
        }
        return toReturn;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooksByUser(User user){
        return bookDao.getAllBooksByUser(user.getUserId()).stream().filter(Book::isAvailable).toList();
    }

    @Override
    @Transactional
    public void setAvailable(Book book, boolean available) {
        bookDao.setAvailable(book, available);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search, String isBookStateFilterActive, String bookStateFilter, long userId) {
        boolean bookStateFilterActive = "true".equalsIgnoreCase(isBookStateFilterActive);

        BookState state = DEFAULT_PUBLICATION_STATE_FILTER;
        if (bookStateFilterActive) {
            state = BookState.fromString(bookStateFilter);
            if (state == null) {
                bookStateFilterActive = false;
            }
        }

        return bookDao.getGenreQtyByBook(search, bookStateFilterActive, state, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookStateWrapper> getBookStateWrapperList(String serach, String isGenreFilterActive, String genreFilter, long userId) {
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        return bookDao.getBookStateQtyByBook(serach, genreFilterActive, genre, userId);
    }

    @Override
    @Transactional
    public Book updateBookState(Long bookId, String bookState) {
        return bookDao.updateBookState(bookId, bookState).orElse(null);
    }
}


















