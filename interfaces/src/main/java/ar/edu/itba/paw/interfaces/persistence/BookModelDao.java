package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;

import java.util.List;
import java.util.Optional;

public interface BookModelDao {

    BookModel createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, long bookCoverId);

    List<Author> createAuthors(List<String> authors);

    void createBookAuthors(List<Long> authorsIds, long bookModelId);

    Optional<BookModel> getBookModelByBookModelId(long bookModelId);

    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType);

    List<GenreWrapper> getGenreQtyByBookModel(String search);
}
