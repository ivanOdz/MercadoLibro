package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class FavoritePublicationNotFoundException extends NotFoundException {
    public FavoritePublicationNotFoundException(String message) {
        super(message);
    }
}
