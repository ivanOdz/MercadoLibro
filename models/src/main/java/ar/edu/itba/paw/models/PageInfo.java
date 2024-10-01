package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.*;

import java.util.List;


public class PageInfo {

    // Search related attributes
    // TODO: Move defaults to Constants file
    private String search = "";
    private boolean isBookStateFilterActive = false;
    private boolean isGenreFilterActive = false;
    private Genre genreFilter;
    private BookState bookStateFilter;
    private SortType sortType = SortType.PUBLICATION_DATE_ASCENDING;

    private List<GenreWrapper> genreWrapperList;
    private List<BookStateWrapper> bookStateWrapperList;

    // Pagination related attributes
    private boolean nextPage, lastPage;
    private int currentPage = 0;    //
    private int maxPage = 1;        // ceil(totalResults/Constants.DEFAULT_PAGE_SIZE);
    private int totalResults = 0;

    public PageInfo(String search, boolean isBookStateFilterActive, boolean isGenreFilterActive, Genre genreFilter, BookState bookStateFilter, SortType sortType, List<GenreWrapper> genreWrapperList, List<BookStateWrapper> bookStateWrapperList, int currentPage, int totalResults) {
        this.search = search;
        this.isBookStateFilterActive = isBookStateFilterActive;
        this.isGenreFilterActive = isGenreFilterActive;
        this.genreFilter = genreFilter;
        this.bookStateFilter = bookStateFilter;
        this.sortType = sortType;
        this.genreWrapperList = genreWrapperList;
        this.bookStateWrapperList = bookStateWrapperList;
        this.currentPage = currentPage;
        this.totalResults = totalResults;
        this.maxPage =  (int) Math.ceil((double) totalResults/Constants.PAGE_SIZE);
    }

    public String getSearch() {
        return search;
    }

    public boolean getIsBookStateFilterActive() {
        return isBookStateFilterActive;
    }

    public boolean getIsGenreFilterActive() {
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

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getTotalResults() {
        return totalResults;
    }
}


// if currentPage != 1, show backPage button
// else show 1 as first element

//if currentPage != maxPage -> show nextPage button

// if totalResults == 0  -> show text : Please try again using less words, Use key words, Only book title name.
