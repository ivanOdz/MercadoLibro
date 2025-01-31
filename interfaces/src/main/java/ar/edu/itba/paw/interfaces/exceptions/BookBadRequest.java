package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookBadRequest extends BadRequestException {
    public BookBadRequest(String message) {
        super(message);
    }
}
