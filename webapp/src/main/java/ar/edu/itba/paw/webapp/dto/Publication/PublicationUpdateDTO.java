package ar.edu.itba.paw.webapp.dto.Publication;

import java.net.URI;

public class PublicationUpdateDTO {

    private URI locationURN;

    public URI getLocationURN() {
        return locationURN;
    }

    public void setLocationURN(URI locationURN) {
        this.locationURN = locationURN;
    }
}
