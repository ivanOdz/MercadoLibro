package ar.edu.itba.paw.interfaces.exceptions;

public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
