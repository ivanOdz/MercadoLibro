package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExchangeForm {
    long bookId;

    @NotBlank
    String location;

    long publicationId;

    public long getBookId() {
        return bookId;
    }

    public @NotBlank String getLocation() {
        return location;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setLocation(@NotBlank String location) {
        this.location = location;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }
}
