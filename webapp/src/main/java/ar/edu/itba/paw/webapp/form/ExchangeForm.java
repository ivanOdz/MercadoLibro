package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ExchangeForm {
    @NotNull
    long bookId;

    @Size(min = 1, max = 255)
    String location;

    long publicationId;

    @NotNull
    public long getBookId() {
        return bookId;
    }

    public @Size(min = 1, max = 255) String getLocation() {
        return location;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public void setBookId(@NotNull long bookId) {
        this.bookId = bookId;
    }

    public void setLocation(@Size(min = 1, max = 255) String location) {
        this.location = location;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }
}
