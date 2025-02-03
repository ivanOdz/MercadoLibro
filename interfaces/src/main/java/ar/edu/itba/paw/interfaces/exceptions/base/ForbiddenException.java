package ar.edu.itba.paw.interfaces.exceptions.base;

public class ForbiddenException extends ApplicationRuntimeException {
	
    public ForbiddenException(String message) {
        super(ExceptionErrorCode.FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(ExceptionErrorCode.FORBIDDEN);
    }
}
