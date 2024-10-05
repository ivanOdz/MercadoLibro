package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;

import java.util.List;

public interface BookModelDao {

    long createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, long bookCoverId);

    List<Long> createAuthors(List<String> authors);

    void createBookAuthors(List<Long> authorsIds, long bookModelId);

    BookModel getBookModelByBookModelId(long bookModelId);

    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType);
}
