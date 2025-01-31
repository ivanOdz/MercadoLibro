//package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookBadRequestException extends BadRequestException {
    public BookBadRequestException(String message) {
        super(message);
    }
}
