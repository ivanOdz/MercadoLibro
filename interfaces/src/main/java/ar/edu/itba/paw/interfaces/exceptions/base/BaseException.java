package ar.edu.itba.paw.interfaces.exceptions.base;

public interface BaseException {
	
    int getStatusCode();
    
    String getExceptionMessage();
}
