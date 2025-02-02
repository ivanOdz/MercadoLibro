package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.FavoritePublication;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class FavoriteDTO {

    private URI publication;

    private URI user;

    private java.util.Date likedAt;

    private URI self;


    public static FavoriteDTO fromFavoritePublication(final UriInfo uriInfo, final FavoritePublication fp) {
        final FavoriteDTO dto = new FavoriteDTO();

        dto.publication = uriInfo.getBaseUriBuilder().path("publications").path(fp.getPublication().getPublicationId().toString()).build();;
        dto.user = uriInfo.getBaseUriBuilder().path("users").path((fp.getUser().getUserId().toString())).build();
        dto.likedAt = fp.getLikedAt();
        dto.self = uriInfo.getBaseUriBuilder().path("publications").path(fp.getPublication().getPublicationId().toString())
                .path("favorite").path(fp.getFavoritepublicationId().toString()).build();

        return dto;
    }


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

    public Date getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Date likedAt) {
        this.likedAt = likedAt;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
