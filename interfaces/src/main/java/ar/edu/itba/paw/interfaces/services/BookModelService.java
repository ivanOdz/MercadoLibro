package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BookModelService {

    long createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                         Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, Integer imageId);

    BookModel getBookModelByBookModelId(long bookModelId);

    List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType);

}
