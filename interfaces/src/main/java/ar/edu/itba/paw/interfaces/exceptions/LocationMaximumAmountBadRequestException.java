package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class LocationMaximumAmountBadRequestException extends BadRequestException {
    public LocationMaximumAmountBadRequestException(String message) {
        super(message);
    }
}
