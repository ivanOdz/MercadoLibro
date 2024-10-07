package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    public Book getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    @Override
    public PaginatedResponse<Book, ItemFilterMetadata> getPaginatedBooks(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int currentPage, long userId, SortType sortType) {
        PaginatedResponse<Book, ItemFilterMetadata> response = bookDao.getPaginatedBooks(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, currentPage, userId, sortType);

        List<BookStateWrapper> bookStateWrapperList = bookDao.getBookStateQtyByBook(search, isGenreFilterActive, genreFilter, userId);
        List<GenreWrapper> genreWrapperList = bookDao.getGenreQtyByBook(search, isBookStateFilterActive, bookStateFilter, userId);


        Map<BookState, Integer> resultByStateMap = bookStateWrapperList.stream()
                .collect(Collectors.toMap(BookStateWrapper::getBookState, BookStateWrapper::getResultByState));

        List<BookStateWrapper> bookStates = new ArrayList<>();
        for (BookState state : BookState.values()) {
            bookStates.add(new BookStateWrapper(state, bookStateService.getBookStateDisplayName(state), resultByStateMap.getOrDefault(state, 0)));
        }

        Map<Genre, Integer> genreByStateMap = genreWrapperList.stream()
                .collect(Collectors.toMap(GenreWrapper::getGenre, GenreWrapper::getResultByGenre));

        List<GenreWrapper> genres = new ArrayList<>();
        for (Genre genre : Genre.values()) {
            genres.add(new GenreWrapper(genre, genreService.getGenreDisplayName(genre), genreByStateMap.getOrDefault(genre, 0)));
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
}


















