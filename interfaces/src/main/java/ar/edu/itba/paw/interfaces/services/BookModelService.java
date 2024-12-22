package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BookModelService {

    BookModel createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition,
                                        Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight);

    BookModel getBookModelByBookModelId(Long bookModelId);

    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, String isGenreFilterActive, String genreFilter, int currentPage, String sortType);

    List<GenreWrapper> getGenreWrapperList(String search);

    BookModel addAuthor(Long bookModelId, String authorName);

    BookModel setCover(Long bookModelId, Long imageId);
}
