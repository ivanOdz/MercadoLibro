package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;
    private final GenreService genreService;

    public BookModelServiceImpl(final BookModelDao bookModelDao, GenreService genreService) {
        this.bookModelDao = bookModelDao;
        this.genreService = genreService;
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
        PaginatedResponse<BookModel, BookModelMetadata> response = bookModelDao.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);

        List<GenreWrapper> genreWrapperList = bookModelDao.getGenreQtyByBookModel(search);

        List<GenreWrapper> genres = new ArrayList<>();
        for (GenreWrapper genre : genreWrapperList) {
            genres.add(new GenreWrapper(genre.getGenre(), genreService.getGenreDisplayName(genre.getGenre()), genre.getResultByGenre()));
        }

        response.getMetadata().setGenreWrapperList(genres);

        return response;

    }
}
