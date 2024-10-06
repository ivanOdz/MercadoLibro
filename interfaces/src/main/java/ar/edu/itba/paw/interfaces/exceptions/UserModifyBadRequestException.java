package ar.edu.itba.paw.interfaces.exceptions;

public class UserModifyBadRequestException extends RuntimeException {
    public UserModifyBadRequestException(String message) {
        super(message);
    }
}
