package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    public BookModelServiceImpl(final BookModelDao bookModelDao) {
        this.bookModelDao = bookModelDao;
    }

    @Autowired
    private MessageSource messageSource;


    @Transactional
    @Override
    public long createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, Integer imageId) {
        List<Long> authorsIds = bookModelDao.createAuthors(authors);

        long bookModelId = bookModelDao.createBookModel(isbn, title, publisher, description, genre, edition, publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, imageId);

        bookModelDao.createBookAuthors(authorsIds, bookModelId);
        return bookModelId;
    }

    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        BookModel bm;
        try {
            bm = bookModelDao.getBookModelByBookModelId(bookModelId);
        } catch (BookModelNotFoundException ex) {
            String errorMessage = messageSource.getMessage("error.bookModelNotFound", new Object[]{bookModelId}, Locale.getDefault());
            throw new BookModelNotFoundException(errorMessage);
        }
        return bm;
    }

    @Override
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType) {
        return bookModelDao.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);
    }
}
