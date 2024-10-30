package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class ExchangeForm {
    @NotNull
    long bookId;

    @NotNull
    long locationId;

    long publicationId;

    @NotNull
    public long getBookId() {
        return bookId;
    }

    public long getLocationId() {
        return locationId;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public void setBookId(@NotNull long bookId) {
        this.bookId = bookId;
    }

    public void setLocation(@NotNull long locationId) {
        this.locationId = locationId;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }
}
