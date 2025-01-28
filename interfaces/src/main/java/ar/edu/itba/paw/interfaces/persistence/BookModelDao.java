package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;

import java.util.List;
import java.util.Optional;

public interface BookModelDao {

    BookModel createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight);

//    List<Author> createAuthors(List<String> authors);

    Optional<BookModel> getBookModelByBookModelId(Long bookModelId);

    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, Genre genre, int currentPage, String sortType);

    List<GenreWrapper> getGenreQtyByBookModel(String search);

    BookModel addAuthor(BookModel bookModel, String authorName);

    BookModel setCover(BookModel bookModel, Image image);
}
