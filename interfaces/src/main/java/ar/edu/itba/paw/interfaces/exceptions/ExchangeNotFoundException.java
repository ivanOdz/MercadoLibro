package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class ExchangeNotFoundException extends NotFoundException {
    public ExchangeNotFoundException(String message) {
        super(message);
    }
}
