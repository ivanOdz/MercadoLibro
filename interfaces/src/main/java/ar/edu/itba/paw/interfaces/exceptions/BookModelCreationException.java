package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class BookModelCreationException extends BadRequestException {
    //TODO: internacionalizar
    public BookModelCreationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
