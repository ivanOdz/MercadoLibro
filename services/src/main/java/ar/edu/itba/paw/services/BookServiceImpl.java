package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    private final BookStateService bookStateService;
    private final GenreService genreService;
    private final BookModelService bookModelService;
    private final ImageService imageService;

    public BookServiceImpl(final BookDao bookDao, BookStateService bookStateService, GenreService genreService, final BookModelService bookModelService, final ImageService imageService) {
        this.bookDao = bookDao;
        this.bookStateService = bookStateService;
        this.genreService = genreService;
        this.bookModelService = bookModelService;
        this.imageService = imageService;
    }

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



        ////////////////////

//
//
//        List<Image> images = new ArrayList<>();
//        if (!newBook){
//            images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));
//        }
//
//        List<Long> imgId = new ArrayList<>();
//        for (Image img : images) {
//            imgId.add(img.getImageId());
//        }
//
//        bookDao.createBookRating(user, bookModelService.getBookModelByBookModelId(bookModelId), rating);
//        Book book = null;
//        try {
//            book = bookDao.createBook(bookModelService.getBookModelByBookModelId(bookModelId), user, bookState, images);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        bookDao.createBookImage(book.getBookId(), imgId);
//
//        return book;
    }

    @Transactional
    @Override
    public Book createNewBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                                  Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                                  Language language, int pages, int weight, BookState bookState, int rating, List<MultipartFile> imageFiles, int bookCoverIndex, User user){
        List<Image> images = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex));

        BookModel bookModel = bookModelService.createBookModel(isbn, title, authors, publisher, description, genre, edition,
                publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, images.get(bookCoverIndex));

        return createBook(bookModel.getBookModelId(), bookState, rating, imageFiles, bookCoverIndex, images, user, true);
    }

    @Transactional
    @Override
    public void exchangeOwnership(Book b1, Book b2) {
        User owner1 = b1.getOwner();
        User owner2 = b2.getOwner();
        bookDao.setOwner(b1, owner2);
        setAvailable(b1, true);
        bookDao.setOwner(b2, owner1);
        setAvailable(b2, true);
    }

    @Override
    public Book getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType) {
        PaginatedResponse<Book, ItemFilterMetadata> response = bookDao.getPaginatedBooks(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, currentPage, userId, sortType);

        List<BookStateWrapper> bookStateWrapperList = bookDao.getBookStateQtyByBook(search, isGenreFilterActive, genreFilter, userId);
        List<GenreWrapper> genreWrapperList = bookDao.getGenreQtyByBook(search, isBookStateFilterActive, bookStateFilter, userId);

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookStateWrapper state : bookStateWrapperList) {
            bookStates.add(new BookStateWrapper(state.getBookState(), bookStateService.getBookStateDisplayName(state.getBookState()), state.getResultByState()));
        }

        List<GenreWrapper> genres = new ArrayList<>();
        for (GenreWrapper genre : genreWrapperList) {
            genres.add(new GenreWrapper(genre.getGenre(), genreService.getGenreDisplayName(genre.getGenre()), genre.getResultByGenre()));
        }

        response.getMetadata().setBookStateWrapperList(bookStates);
        response.getMetadata().setGenreWrapperList(genres);

        return response;

    }

    public List<MultipartFile> arrangeImages(List<MultipartFile> images, int bookCoverIndex) {
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

    public List<Book> getAvailableBooksByUser(User user){
        return bookDao.getAllBooksByUser(user.getUserId()).stream().filter(Book::isAvailable).toList();
    }

    @Override
    public void setAvailable(Book book, boolean available) {
        bookDao.setAvailable(book, available);
    }
}


















