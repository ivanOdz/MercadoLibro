package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    private final BookModelService bookModelService;
    private final ImageService imageService;

    public BookServiceImpl(final BookDao bookDao, final BookModelService bookModelService, final ImageService imageService) {
        this.bookDao = bookDao;
        this.bookModelService = bookModelService;
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public Number createBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, BookState bookState, int edition,
                             int rating, List<MultipartFile> imageFiles, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                             Language language, int pages, int weight, int bookCoverIndex, boolean publish, User user, Long bookModelId) {

        List<Integer> imagesId = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex)).stream().map(Image::getImageId).toList();

        Long bmId = bookModelId;

        // CHECK: implementation

        if(bmId == null) {
            bmId = bookModelService.createBookModel(isbn, title, authors, publisher, description, genre, edition,
                    publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, imagesId.get(bookCoverIndex));
        }
        bookDao.createBookRating(user, bmId, rating);

        Number toReturn = bookDao.createBook(bmId, user, bookState, imagesId);

        bookDao.createBookImage(toReturn.longValue(), imagesId);

        return toReturn;
    }

    @Transactional
    @Override
    public void exchangeOwnership(Book b1, Book b2) {
        bookDao.setOwner(b1.getBookId(), b2.getOwner().getUserId());
        bookDao.setOwner(b2.getBookId(), b1.getOwner().getUserId());
    }

    @Override
    public Optional<Book> getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType) {
        return bookDao.getPaginatedBooks(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, currentPage, userId, sortType);
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
}


















