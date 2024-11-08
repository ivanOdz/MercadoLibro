package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class ExchangeForm {
    @NotNull
    private Long bookId;

    @NotNull
    private Long locationId;

    public void setLocationId(@NotNull Long locationId) {
        this.locationId = locationId;
    }

    private Long publicationId;

    public @NotNull Long getBookId() {
        return bookId;
    }

    public @NotNull Long getLocationId() {
        return locationId;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setBookId(@NotNull Long bookId) {
        this.bookId = bookId;
    }

    public void setLocation(@NotNull long locationId) {
        this.locationId = locationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }
}
