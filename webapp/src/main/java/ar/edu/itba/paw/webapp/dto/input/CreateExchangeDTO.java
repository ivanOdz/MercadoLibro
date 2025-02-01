package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class CreateExchangeDTO {
    private Long bookId;
    private Long locationId;
    private Long publicationId;

    public void setPublicationUrn(URI publicationUrn) {
        this.publicationId = UrnResolverUtil.getPublicationId(publicationUrn);
    }

    public void setLocationUrn(URI locationUrn) {
        this.locationId = UrnResolverUtil.getLocationId(locationUrn);
    }

    public void setBookUrn(URI bookUrn) {
        this.bookId = UrnResolverUtil.getBookId(bookUrn);
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Long getPublicationId() {
        return publicationId;
    }
}
