package ar.edu.itba.paw.interfaces.exceptions.base;

import org.springframework.web.client.HttpClientErrorException;

import static ar.edu.itba.paw.interfaces.exceptions.base.ExceptionErrorCode.UNAUTHORIZED;

public class UnauthorizedException extends ApplicationRuntimeException{
    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(UNAUTHORIZED, message);
    }


}
