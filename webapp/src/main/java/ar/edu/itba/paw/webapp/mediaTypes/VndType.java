package ar.edu.itba.paw.webapp.mediaTypes;

public class VndType {
    public VndType() {
        throw new AssertionError();
    }

    public static final String APPLICATION_USER = "application/vnd.users.v1+json";
    public static final String APPLICATION_BOOK = "application/vnd.books.v1+json";
    public static final String APPLICATION_BOOK_MODEL = "application/vnd.book_models.v1+json";
    public static final String APPLICATION_BOOK_COVER = "application/vnd.book_models.covers.v1+json";
    public static final String APPLICATION_AUTHOR = "application/vnd.books.book_models.authors.v1+json";
    public static final String APPLICATION_GENRE = "application/vnd.books.book_models.genres.v1+json";
    public static final String APPLICATION_BOOK_STATE = "application/vnd.books.state.S.v1+json";
    public static final String APPLICATION_PUBLICATION = "application/vnd.publications.v1+json";
    public static final String APPLICATION_EXCHANGE = "application/vnd.exchanges.v1+json";
}
