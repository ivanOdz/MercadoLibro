package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class UserReviewNotFound extends NotFoundException {
    public UserReviewNotFound(String message) {
        super(message);
    }
}
