package ar.edu.itba.paw.webapp.dto.Publication;

import java.net.URI;

public class FavoriteCreationDTO {

    private URI publication;

    private URI user;

    public URI getPublication() {
        return publication;
    }

    public void setPublication(URI publication) {
        this.publication = publication;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
