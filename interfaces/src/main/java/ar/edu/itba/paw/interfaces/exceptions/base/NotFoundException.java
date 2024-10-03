package ar.edu.itba.paw.interfaces.exceptions.base;

public class NotFoundException extends ApplicationRuntimeException {
    public NotFoundException(String message) {
        super(ExceptionErrorCode.NOT_FOUND, message);
    }
}
