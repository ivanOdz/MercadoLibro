package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;

import java.util.List;

public interface BookModelDao {
    BookModel getBookModelByBookModelId(long bookModelId);

    //BookModel addBookModel(String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language language, BookDimension dimension, Short publicationYear, boolean pocketEdition, boolean hardcover);

    List<BookModel> getBookModelByUserId(long userId);

    List<BookModel> getAllBookModel(String search, int genreFilter);

    Rating getRatingByBookModelId(long bookModelId);

    //----------------------- Adaptadas -----------------------------------------//

    List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType);

}

