package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class BookModelNotFoundException extends NotFoundException {
    public BookModelNotFoundException(String message) {
        super(message);
    }
}
