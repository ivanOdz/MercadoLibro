package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class PublicationBadRequestException extends BadRequestException {
    public PublicationBadRequestException(String message) {
        super(message);
    }
}
