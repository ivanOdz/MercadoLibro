package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;

public class ExchangeBadRequestException extends BadRequestException {
    public ExchangeBadRequestException(String message) {
        super(message);
    }
}
