package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Book;

public class ExchangeForm {
    long bookId;
    String location;
    long publicationId;

    public long getBookId() {
        return bookId;
    }

    public String getLocation() {
        return location;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }
}
