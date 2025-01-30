package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.utils.UrnResolverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_GENRE_FILTER;

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
    public Optional<BookModel> getBookModelByBookModelId(Long bookModelId) {
        LOGGER.info("Fetching BookModel with ID: {}", bookModelId);

        Optional<BookModel> bookModel = bookModelDao.getBookModelByBookModelId(bookModelId);
        /*if(bookModel.isEmpty()){
            LOGGER.warn("BookModel not found for ID: {}", bookModelId);
            throw new BookModelNotFoundException("Book model not found");
        }
        LOGGER.info("Successfully retrieved BookModel with ID: {}", bookModelId);*/
        return bookModel;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, String genre, int currentPage, String sortType) {
        boolean genreFilterActive = genre != null;

        Genre genre_filter = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre_filter = Genre.fromString(genre);
            if(genre_filter == null){
                genreFilterActive = false;
            }
        }

        return bookModelDao.getPaginatedBookModels(search, genre_filter, currentPage, sortType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search) {
        return bookModelDao.getGenreQtyByBookModel(search);
    }

    @Transactional
    @Override
    public BookModel setCover(Long bookModelId, URI imageUrn) {
        Optional<BookModel> bookModel = getBookModelByBookModelId(bookModelId);
        
        if(bookModel.isPresent()) {
	        Image image = imageService.getImageById(UrnResolverUtil.getImageId(imageUrn));
	        return bookModelDao.setCover(bookModel.get(), image);
        }
        return null;
    }
}
