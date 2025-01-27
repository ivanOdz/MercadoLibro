package ar.edu.itba.paw.webapp.dto.Publication;

import java.net.URI;
import java.sql.Timestamp;

public class LikeDTO {

    private URI publication;

    private URI user;

    private Timestamp likedAt;

    private URI self;

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

    public Timestamp getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Timestamp likedAt) {
        this.likedAt = likedAt;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
