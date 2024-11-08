package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class LocationNotFoundException extends NotFoundException {
    public LocationNotFoundException(String message) {
        super(message);
    }
}
