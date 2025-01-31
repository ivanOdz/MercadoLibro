package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class BookModelNotFound extends NotFoundException {
    public BookModelNotFound(String message) {
        super(message);
    }
}
