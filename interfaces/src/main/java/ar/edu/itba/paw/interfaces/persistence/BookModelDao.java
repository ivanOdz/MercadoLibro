package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookModelDao {
    BookModel getBookModelByBookModelId(long bookModelId);

    List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType);

    long createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, long bookCoverId);

    List<Long> createAuthors(List<String> authors);

    void createBookAuthors(List<Long> authorsIds, long bookModelId);

    }

