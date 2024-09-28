package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BookModelService {

    BookModel getBookModelByBookModelId(long bookModelId);


    List<BookModel> getBookModelByUserId(long userId);

    List<BookModel> getAllBookModelFilteredBy(String search, int genreFilter);

    Rating getRatingByBookModelId(long bookModelId);

    //-----------------ADAPTADO------------------------//

    List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType);

    long createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition,
                              Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, List<MultipartFile> images, long bookCoverIndex);

}
