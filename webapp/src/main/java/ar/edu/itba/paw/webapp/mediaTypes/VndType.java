package ar.edu.itba.paw.webapp.mediaTypes;

public class VndType {
    public VndType() {
        throw new AssertionError();
    }

    public static final String APPLICATION_USER = "application/vnd.users.v1+json";
    public static final String APPLICATION_VERIFICATION_CODE = "application/vnd.verification.code.v1+json";
    public static final String APPLICATION_USER_PASSWORD = "application/vnd.users.password.v1+json";
    public static final String APPLICATION_USER_EMAIL = "application/vnd.users.email.v1+json";
    public static final String APPLICATION_LOCATION = "application/vnd.location.v1+json";

    // Lists
    public static final String APPLICATION_LIST_LOCATION = "application/vnd.list.locations.v1+json";
    public static final String APPLICATION_LIST_USER_REVIEW = "application/vnd.list.user.review.v1+json";

    //public static final String USER_LANGUAGE = "application/vnd.users.language.v1+json";
    //public static final String USER_USERNAME = "application/vnd.users.username.v1+json";
    public static final String APPLICATION_BOOK = "application/vnd.books.v1+json";
    public static final String APPLICATION_BOOK_MODEL = "application/vnd.book_models.v1+json";
    public static final String APPLICATION_BOOK_COVER = "application/vnd.book_models.covers.v1+json";
    public static final String APPLICATION_AUTHOR = "application/vnd.books.book_models.authors.v1+json";
    public static final String APPLICATION_GENRE = "application/vnd.books.book_models.genres.v1+json";
    public static final String APPLICATION_BOOK_STATE = "application/vnd.books.state.S.v1+json";
    public static final String APPLICATION_PUBLICATION = "application/vnd.publications.v1+json";
    public static final String APPLICATION_EXCHANGE = "application/vnd.exchanges.v1+json";
    public static final String APPLICATION_UPDATE_EXCHANGE = "application/vnd.exchanges.update.v1+json";
    public static final String APPLICATION_CONFIRM_EXCHANGE = "application/vnd.exchanges.confirmation.v1+json";
    public static final String APPLICATION_GENRE_SUMMARY = "application/vnd.genre.summary.v1+json";
    public static final String APPLICATION_CONDITION_SUMMARY = "application/vnd.genre.summary.v1+json";
    public static final String APPLICATION_MESSAGE = "application/vnd.message.v1+json";
    public static final String APPLICATION_USER_REVIEW = "application/vnd.user.review.v1+json";
}
