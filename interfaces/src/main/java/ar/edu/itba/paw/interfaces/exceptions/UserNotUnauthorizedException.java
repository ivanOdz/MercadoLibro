package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.UnauthorizedException;

public class UserNotUnauthorizedException extends UnauthorizedException {
	
    public UserNotUnauthorizedException(String message) {
        super(message);
    }
}
