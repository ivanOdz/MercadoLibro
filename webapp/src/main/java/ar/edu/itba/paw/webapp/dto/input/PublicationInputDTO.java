package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class PublicationInputDTO {

    private Long userId;

    private Long bookId;

    private Long locationId;


    public void setUserId(URI userURN) {
        this.userId = UrnResolverUtil.getUserId(userURN);
    }

    public void setBookId(URI bookURN) {
        this.bookId = UrnResolverUtil.getBookId(bookURN);
    }

    public void setLocationId(URI locationURN) {
        this.locationId = UrnResolverUtil.getLocationId(locationURN);
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getLocationId() {
        return locationId;
    }
}
