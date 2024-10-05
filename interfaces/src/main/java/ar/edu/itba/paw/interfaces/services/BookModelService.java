package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookModelService {

    long createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                         Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, Integer imageId);

    BookModel getBookModelByBookModelId(long bookModelId);

    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType);
}
