package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class UserRatingNotFound extends NotFoundException {
    public UserRatingNotFound(String message) {
        super(message);
    }
}
