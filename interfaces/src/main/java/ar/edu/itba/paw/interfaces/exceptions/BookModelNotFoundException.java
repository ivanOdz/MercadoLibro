package ar.edu.itba.paw.interfaces.exceptions;

public class BookModelNotFoundException extends RuntimeException {

    public BookModelNotFoundException(String messsage) {
        super(messsage);
    }

    public BookModelNotFoundException() {
        super();
    }
}
