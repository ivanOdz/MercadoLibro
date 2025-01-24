package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.*;

import java.util.List;

public class ItemFilterMetadata extends GenreFilterMetadata{

    private final boolean isBookStateFilterActive;
    private final BookState bookStateFilter;
    private List<BookStateWrapper> bookStateWrapperList;

    public ItemFilterMetadata(int currentPage, int pageSize, int totalResults, String search, Genre genre, SortType sortType, List<GenreWrapper> genreWrapperList, BookState state, List<BookStateWrapper> bookStateWrapperList) {
        super(currentPage, pageSize, totalResults, search, genre, sortType, genreWrapperList);
        this.isBookStateFilterActive = state != null;
        this.bookStateFilter = state;
        this.bookStateWrapperList = bookStateWrapperList;
    }

    public boolean getIsBookStateFilterActive() {
        return isBookStateFilterActive;
    }

    public BookState getBookStateFilter() {
        return bookStateFilter;
    }

    public List<BookStateWrapper> getBookStateWrapperList() {
        return bookStateWrapperList;
    }

    public void setBookStateWrapperList(List<BookStateWrapper> bookStateWrapperList) {
        this.bookStateWrapperList = bookStateWrapperList;
    }
}
