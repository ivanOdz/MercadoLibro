package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookModelService {

    BookModel getBookModelByBookModelId(long bookModelId);

    //BookModel addBookModel(List<String> authors, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language language, BookDimension dimension, Short publicationYear, boolean pocketEdition, boolean hardcover);

    List<BookModel> getBookModelByUserId(long userId);

    List<BookModel> getAllBookModelFilteredBy(String search, int genreFilter);

    Rating getRatingByBookModelId(long bookModelId);

    //-----------------ADAPTADO------------------------//

    List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType);

}
