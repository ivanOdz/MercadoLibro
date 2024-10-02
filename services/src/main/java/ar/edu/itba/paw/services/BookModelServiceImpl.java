package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    public BookModelServiceImpl(BookModelDao bookModelDao) {
        this.bookModelDao = bookModelDao;
    }

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
        return bookModelDao.getBookModelByBookModelId(bookModelId);
    }

    @Override
    public List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType) {
        return bookModelDao.getFilteredSortedOrderedModelBooksByPage(search, isGenreFilterActive, genreFilter, pageIndex, sortType);
    }
}
