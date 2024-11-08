package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class ImageBadRequestException extends BadRequestException {
    public ImageBadRequestException(String message) {
        super(message);
    }
}
