package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.validation.SupportedBookState;

public class BookStateDTO {
    @SupportedBookState
    String bookState;

    public String getBookState() {
        return bookState;
    }

    public void setBookState(String bookState) {
        this.bookState = bookState;
    }
}
