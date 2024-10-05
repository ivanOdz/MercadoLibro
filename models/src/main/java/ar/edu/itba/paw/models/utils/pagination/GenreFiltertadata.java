package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.*;

import java.util.List;

public abstract class GenreFiltertadata extends Metadata{

    private String search;
    private boolean isGenreFilterActive;
    private Genre genreFilter;
    private BookState bookStateFilter;
    private SortType sortType;

    private List<GenreWrapper> genreWrapperList;


    public ItemFilterMetadata(int currentPage, int maxPage, int totalResults, String search, boolean isBookStateFilterActive, boolean isGenreFilterActive, Genre genreFilter, BookState bookStateFilter, SortType sortType, List<GenreWrapper> genreWrapperList, List<BookStateWrapper> bookStateWrapperList) {
        super(currentPage, maxPage, totalResults);
        this.search = search;
        this.isGenreFilterActive = isGenreFilterActive;
        this.genreFilter = genreFilter;
        this.bookStateFilter = bookStateFilter;
        this.sortType = sortType;
        this.genreWrapperList = genreWrapperList;
    }

    public String getSearch() {
        return search;
    }

    public boolean isBookStateFilterActive() {
        return isBookStateFilterActive;
    }

    public boolean isGenreFilterActive() {
        return isGenreFilterActive;
    }

    public Genre getGenreFilter() {
        return genreFilter;
    }

    public BookState getBookStateFilter() {
        return bookStateFilter;
    }

    public SortType getSortType() {
        return sortType;
    }

    public List<GenreWrapper> getGenreWrapperList() {
        return genreWrapperList;
    }

    public List<BookStateWrapper> getBookStateWrapperList() {
        return bookStateWrapperList;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setBookStateFilterActive(boolean bookStateFilterActive) {
        isBookStateFilterActive = bookStateFilterActive;
    }

    public void setGenreFilterActive(boolean genreFilterActive) {
        isGenreFilterActive = genreFilterActive;
    }

    public void setGenreFilter(Genre genreFilter) {
        this.genreFilter = genreFilter;
    }

    public void setBookStateFilter(BookState bookStateFilter) {
        this.bookStateFilter = bookStateFilter;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public void setGenreWrapperList(List<GenreWrapper> genreWrapperList) {
        this.genreWrapperList = genreWrapperList;
    }

    public void setBookStateWrapperList(List<BookStateWrapper> bookStateWrapperList) {
        this.bookStateWrapperList = bookStateWrapperList;
    }
}
