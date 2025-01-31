package ar.edu.itba.paw.webapp.dto.User;

import java.net.URI;

public class LocationsByPublicationDTO {

    private URI publicationURN;

    public URI getPublicationURN() {
        return publicationURN;
    }

    public void setPublicationURN(URI publicationURN) {
        this.publicationURN = publicationURN;
    }
}
