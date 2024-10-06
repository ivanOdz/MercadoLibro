package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
