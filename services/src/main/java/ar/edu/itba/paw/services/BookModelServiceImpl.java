package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_BOOK_GENRE_FILTER;

@Service
public class BookModelServiceImpl implements BookModelService {

    @Autowired
    private BookModelDao bookModelDao;

    @Autowired
    private ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookModelServiceImpl.class);

    @Override
    @Transactional
    public BookModel createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear,
                                     boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, List<String> authors) {
        LOGGER.info("Starting creation of BookModel with ISBN: {}", isbn);

        BookModel bookModelOpt = bookModelDao.createBookModel(isbn, title, publisher, description, genre, edition, publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight);
        LOGGER.info("BookModel created successfully with ISBN: {}", isbn);

        for(String author : authors){
            bookModelDao.addAuthor(bookModelOpt, author);
        }
        return bookModelOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public BookModel getBookModelByBookModelId(Long bookModelId) {
        LOGGER.info("Fetching BookModel with ID: {}", bookModelId);

        Optional<BookModel> bookModel = bookModelDao.getBookModelByBookModelId(bookModelId);
        if(bookModel.isEmpty()){
            LOGGER.warn("BookModel not found for ID: {}", bookModelId);
            throw new BookModelNotFoundException("Book model not found");
        }
        LOGGER.info("Successfully retrieved BookModel with ID: {}", bookModelId);
        return bookModel.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, String genre, int currentPage, String sortType) {
        Genre genreFilter = Genre.fromString(genre);
        genreFilter = genreFilter == null ? DEFAULT_BOOK_GENRE_FILTER : genreFilter;

        return bookModelDao.getPaginatedBookModels(search, genreFilter, currentPage, sortType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search) {
        return bookModelDao.getGenreQtyByBookModel(search);
    }

    @Override
    @Transactional
    public BookModel setCover(Long bookModelId, Long imageId) {
        BookModel bookModel = getBookModelByBookModelId(bookModelId);

        Image image = imageService.getImageById(imageId);
        return bookModelDao.setCover(bookModel, image);
    }
}
