package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.utils.UrnResolverUtil;

import java.net.URI;

public class PublicationInputDTO {

    private URI userURN;

    private URI bookURN;

    private URI locationURN;

    public void setUserURN(URI userURN) {
        this.userURN = userURN;
    }

    public void setBookURN(URI bookURN) {
        this.bookURN = bookURN;
    }

    public void setLocationURN(URI locationURN) {
        this.locationURN = locationURN;
    }

    public Long getUserURN() {
        return UrnResolverUtil.getUserId(userURN);
    }

    public Long getBookURN() {
        return UrnResolverUtil.getBookId(bookURN);
    }

    public Long getLocationURN() {
        return UrnResolverUtil.getLocationId(locationURN);
    }
}
