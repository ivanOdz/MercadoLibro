package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookAuthorBadRequestException extends BadRequestException {
    public BookAuthorBadRequestException(String message) {
        super(message);
    }
}
