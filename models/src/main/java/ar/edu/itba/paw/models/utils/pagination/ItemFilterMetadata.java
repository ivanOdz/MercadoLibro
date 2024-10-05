package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.*;

import java.util.List;

public class ItemFilterMetadata extends GenreFilterMetadata{

    private final boolean isBookStateFilterActive;
    private final BookState bookStateFilter;
    private final List<BookStateWrapper> bookStateWrapperList;

    public ItemFilterMetadata(int currentPage, int maxPage, int totalResults, String search, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, List<GenreWrapper> genreWrapperList, boolean isBookStateFilterActive, BookState bookStateFilter, List<BookStateWrapper> bookStateWrapperList) {
        super(currentPage, maxPage, totalResults, search, isGenreFilterActive, genreFilter, sortType, genreWrapperList);
        this.isBookStateFilterActive = isBookStateFilterActive;
        this.bookStateFilter = bookStateFilter;
        this.bookStateWrapperList = bookStateWrapperList;
    }

    public boolean isBookStateFilterActive() {
        return isBookStateFilterActive;
    }

    public BookState getBookStateFilter() {
        return bookStateFilter;
    }

    public List<BookStateWrapper> getBookStateWrapperList() {
        return bookStateWrapperList;
    }
}
