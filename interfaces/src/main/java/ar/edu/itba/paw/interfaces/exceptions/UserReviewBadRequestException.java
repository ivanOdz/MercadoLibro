package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class UserReviewBadRequestException extends BadRequestException {
    public UserReviewBadRequestException(String message) {
        super(message);
    }
}
