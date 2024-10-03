package ar.edu.itba.paw.interfaces.exceptions.base;

public class ApplicationRuntimeException extends RuntimeException implements BaseException {
    private final int statusCode;
    private final String exceptionMessage;

    public ApplicationRuntimeException(int statusCode, String exceptionMessage) {
        super();
        this.statusCode = statusCode;
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
