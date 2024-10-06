package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookModelBadRequestException extends BadRequestException {

    public BookModelBadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
