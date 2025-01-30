package ar.edu.itba.paw.interfaces.exceptions.base;


public class UnauthorizedException extends ApplicationRuntimeException {
	
    public UnauthorizedException() {
        super(ExceptionErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(ExceptionErrorCode.UNAUTHORIZED, message);
    }
}
