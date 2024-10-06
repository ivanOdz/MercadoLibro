package ar.edu.itba.paw.interfaces.exceptions.base;

public final class ExceptionErrorCode {

    public static final int BAD_REQUEST= 400;

    public static final int UNAUTHORIZED = 401;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND= 404;

    public static final int INTERNAL_SERVER_ERROR = 500;


    private ExceptionErrorCode() {
        // Private constructor to hide the implicit public one
    }
}
