package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class UserVerificationBadRequestException extends BadRequestException {
    public UserVerificationBadRequestException(String message) {
        super(message);
    }
}
