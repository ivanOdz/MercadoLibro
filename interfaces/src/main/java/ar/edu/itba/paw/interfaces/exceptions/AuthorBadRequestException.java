package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class AuthorBadRequestException extends BadRequestException {
    public AuthorBadRequestException(String message) {
        super(message);
    }
}
