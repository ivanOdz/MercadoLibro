package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    public BookModelServiceImpl(final BookModelDao bookModelDao) {
        this.bookModelDao = bookModelDao;
    }

    @Transactional
    @Override
    public long createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, Integer imageId) {
        // CHECK: if missing controller catch blocks
        List<Long> authorsIds = bookModelDao.createAuthors(authors);

        // CHECK: if missing controller catch blocks
        long bookModelId = bookModelDao.createBookModel(isbn, title, publisher, description, genre, edition, publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, imageId);

        // CHECK: if missing controller catch blocks
        bookModelDao.createBookAuthors(authorsIds, bookModelId);
        return bookModelId;
    }

    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        // CHECK: if missing controller catch blocks
        return bookModelDao.getBookModelByBookModelId(bookModelId);
    }

    @Override
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType) {
        return bookModelDao.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);
    }
}
