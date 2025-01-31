package ar.edu.itba.paw.webapp.dto.input;

import java.net.URI;

public class CreateExchangeDTO {
    private URI bookUrn;
    private URI locationUrn;

    public URI getPublicationUrn() {
        return publicationUrn;
    }

    public void setPublicationUrn(URI publicationUrn) {
        this.publicationUrn = publicationUrn;
    }

    public URI getLocationUrn() {
        return locationUrn;
    }

    public void setLocationUrn(URI locationUrn) {
        this.locationUrn = locationUrn;
    }

    public URI getBookUrn() {
        return bookUrn;
    }

    public void setBookUrn(URI bookUrn) {
        this.bookUrn = bookUrn;
    }

    private URI publicationUrn;


}
