package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class MessageNotFoundException extends NotFoundException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
