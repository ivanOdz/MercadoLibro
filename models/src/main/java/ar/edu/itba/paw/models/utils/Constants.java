package ar.edu.itba.paw.models.utils;


public final class Constants {

    private Constants() {
        // restrict instantiation
    }

    public static final int BOOKS_PAGE_SIZE = 15;

    public static final int PUBLICATIONS_PAGE_SIZE = 5;

    public static final int EXCHANGES_PAGE_SIZE = 5;

    public static final int PROFILE_PAGE_SIZE = 6;

    public static final int INITIAL_EXCHANGE_VALUE = 0;

    public static final boolean INITIAL_AVAILABLE_VALUE = true;

    public static final SortType DEFAULT_PUBLICATION_SORT_TYPE = SortType.PUBLICATION_DATE_DESCENDING;

    public static final BookState DEFAULT_PUBLICATION_STATE_FILTER = BookState.NEW;

    public static final Genre DEFAULT_PUBLICATION_GENRE_FILTER = Genre.WESTERN;
}
