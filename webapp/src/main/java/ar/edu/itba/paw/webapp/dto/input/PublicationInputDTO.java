package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class PublicationInputDTO {

    private URI userURN;

    private URI bookURN;

    private URI locationURN;

    public Long getUserId() {
        return UrnResolverUtil.getUserId(userURN);
    }

    public Long getBookId() {
        return UrnResolverUtil.getBookId(bookURN);
    }

    public Long getLocationId() {
        return UrnResolverUtil.getLocationId(locationURN);
    }

    public URI getUserURN() {
        return userURN;
    }

    public void setUserURN(URI userURN) {
        this.userURN = userURN;
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
}
