package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.utils.*;
import org.springframework.web.bind.annotation.RequestParam;


public class PageInfo {

    // Search related attributes
    private String search = "";
    private boolean isBookStateFilterActive = false;
    private boolean isGenreFilterActive = false;
    private Genre genreFilter;
    private BookState bookState;
    private SortType sortType = SortType.PUBLICATION_DATE_ASCENDING;

    // Pagination related attributes
    private int currentPage = 0;
    private int maxPage = 1;
    private int totalResults = 0;

    public PageInfo(String search, boolean isBookStateFilterActive, boolean isGenreFilterActive, Genre genreFilter, BookState bookState, SortType sortType, int currentPage, int maxPage, int totalResults) {
        this.search = search;
        this.isBookStateFilterActive = isBookStateFilterActive;
        this.isGenreFilterActive = isGenreFilterActive;
        this.genreFilter = genreFilter;
        this.bookState = bookState;
        this.sortType = sortType;
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.totalResults = totalResults;
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

    public BookState getBookState() {
        return bookState;
    }

    public SortType getSortType() {
        return sortType;
    }

    // + 1 to make 1 first page and not 0
    public int getCurrentPage() {
        return currentPage + 1;
    }

    // To be consistent with getCurrentPage
    public int getMaxPage() {
        return maxPage + 1;
    }

    public int getTotalResults() {
        return totalResults;
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

    public void setBookState(BookState bookState) {
        this.bookState = bookState;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    // Pagination extra methods

    public boolean hasNextPage() {
        return currentPage < maxPage;
    }

    public boolean hasPreviousPage() {
        return currentPage > 0;
    }

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
        }
    }

    public void previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
        }
    }
}


// if currentPage != 1, show backPage button
// else show 1 as first element

//if currentPage != maxPage -> show nextPage button

// if totalResults == 0  -> show text : Please try again using less words, Use key words, Only book title name.
