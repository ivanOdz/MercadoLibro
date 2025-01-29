package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.SortType;

import java.util.List;

public class BookModelMetadata extends GenreFilterMetadata {

    public BookModelMetadata(int currentPage, int pageSize, int totalResults, String search, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, List<GenreWrapper> genreWrapperList) {
        super(currentPage, pageSize, totalResults, search, genreFilter, sortType, genreWrapperList);
    }
}
