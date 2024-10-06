package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class UserModifyBadRequestException extends BadRequestException {
    public UserModifyBadRequestException(String message) {
        super(message);
    }
}
