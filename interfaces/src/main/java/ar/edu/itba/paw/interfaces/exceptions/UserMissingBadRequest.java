package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;


public class UserMissingBadRequest extends BadRequestException {
    public UserMissingBadRequest(String message) {
        super(message);
    }
}
