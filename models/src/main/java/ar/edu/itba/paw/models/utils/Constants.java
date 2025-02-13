package ar.edu.itba.paw.models.utils;


public final class Constants {

    private Constants() {
        // restrict instantiation
    }

    public static final int BOOKS_PAGE_SIZE = 12;

    public static final int PUBLICATIONS_PAGE_SIZE = 5;

    public static final int EXCHANGES_PAGE_SIZE = 3;

    public static final int PROFILE_PAGE_SIZE = 3;

    public static final int INITIAL_EXCHANGE_VALUE = 0;

    public static final boolean INITIAL_AVAILABLE_VALUE = true;

    public static final SortType DEFAULT_PUBLICATION_SORT_TYPE = SortType.PUBLICATION_DATE_DESCENDING;

    public static final BookState DEFAULT_PUBLICATION_STATE_FILTER = BookState.NEW;

    public static final Genre DEFAULT_PUBLICATION_GENRE_FILTER = Genre.WESTERN;

    public static final BookState DEFAULT_BOOK_STATE_FILTER = BookState.NEW;

    public static final Genre DEFAULT_BOOK_GENRE_FILTER = Genre.WESTERN;

    public static final SortType DEFAULT_BOOK_SORT_TYPE = SortType.RATING_DESCENDING;
}
