package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.*;

import java.util.List;

public abstract class GenreFilterMetadata extends Metadata{

    protected String search;
    protected boolean isGenreFilterActive;
    protected Genre genreFilter;
    protected SortType sortType;

    protected List<GenreWrapper> genreWrapperList;

    public GenreFilterMetadata(int currentPage, int maxPage, int totalResults, String search, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, List<GenreWrapper> genreWrapperList) {
        super(currentPage, maxPage, totalResults);
        this.search = search;
        this.isGenreFilterActive = isGenreFilterActive;
        this.genreFilter = genreFilter;
        this.sortType = sortType;
        this.genreWrapperList = genreWrapperList;
    }

    public String getSearch() {
        return search;
    }

    public boolean isGenreFilterActive() {
        return isGenreFilterActive;
    }

    public Genre getGenreFilter() {
        return genreFilter;
    }

    public SortType getSortType() {
        return sortType;
    }

    public List<GenreWrapper> getGenreWrapperList() {
        return genreWrapperList;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setGenreFilterActive(boolean genreFilterActive) {
        isGenreFilterActive = genreFilterActive;
    }

    public void setGenreFilter(Genre genreFilter) {
        this.genreFilter = genreFilter;
    }


    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public void setGenreWrapperList(List<GenreWrapper> genreWrapperList) {
        this.genreWrapperList = genreWrapperList;
    }

}
