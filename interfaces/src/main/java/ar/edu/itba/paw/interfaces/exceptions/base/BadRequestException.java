package ar.edu.itba.paw.interfaces.exceptions.base;

public class BadRequestException extends ApplicationRuntimeException {

    public BadRequestException(String exceptionMessage) {
        super(ExceptionErrorCode.BAD_REQUEST, exceptionMessage);
    }
}