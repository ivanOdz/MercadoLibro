package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class CreateExchangeDTO {
    private URI bookURN;
    private URI locationURN;
    private URI publicationURN;

    public Long getBookId() {
        return UrnResolverUtil.getBookId(bookURN);
    }

    public Long getLocationId() {
        return UrnResolverUtil.getLocationId(locationURN);
    }

    public Long getPublicationId() {
        return UrnResolverUtil.getPublicationId(publicationURN);
    }

    public URI getBookURN() {
        return bookURN;
    }

    public void setBookURN(URI bookURN) {
        this.bookURN = bookURN;
    }

    public URI getLocationURN() {
        return locationURN;
    }

    public void setLocationURN(URI locationURN) {
        this.locationURN = locationURN;
    }

    public URI getPublicationURN() {
        return publicationURN;
    }

    public void setPublicationURN(URI publicationURN) {
        this.publicationURN = publicationURN;
    }
}
