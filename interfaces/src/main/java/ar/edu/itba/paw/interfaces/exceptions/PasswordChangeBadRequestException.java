package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class PasswordChangeBadRequestException extends BadRequestException {
    public PasswordChangeBadRequestException(String message) {
        super(message);
    }
}
