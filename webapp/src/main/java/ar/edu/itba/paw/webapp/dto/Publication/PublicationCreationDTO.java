package ar.edu.itba.paw.webapp.dto.Publication;

import java.net.URI;

public class PublicationCreationDTO {

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

    public URI getUserURN() {
        return userURN;
    }

    public URI getBookURN() {
        return bookURN;
    }

    public URI getLocationURN() {
        return locationURN;
    }
}
