package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookModelCreationException extends BadRequestException {

    public BookModelCreationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
