package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class PublicationDTO {
    private URI book;
    private PublicationState publicationState;
    private Date publicationDatetime;
    private URI locations;
    private URI user;
    private URI favoriteEndpoint;

    private URI self;


    public static PublicationDTO fromPublication(final UriInfo uriInfo, final Publication publication) {
        final PublicationDTO dto = new PublicationDTO();

        dto.publicationState = publication.getPublicationState();
        dto.publicationDatetime = publication.getPublicationDatetime();

        dto.self = URI.create("/publications/" + publication.getPublicationId());
        dto.book = URI.create("/books/" + publication.getBook().getBookId());
        dto.user = URI.create("/users/" + publication.getUser().getUserId());

        dto.locations = URI.create("/users/" + publication.getUser().getUserId() + "/locations?publication_id=" + publication.getPublicationId());
        dto.favoriteEndpoint = URI.create("/publications/" + publication.getPublicationId() + "/favorite");

        return dto;
    }

    public URI getFavoriteEndpoint() {
        return favoriteEndpoint;
    }

    public void setFavoriteEndpoint(URI favoriteEndpoint) {
        this.favoriteEndpoint = favoriteEndpoint;
    }

    public URI getBook() {
        return book;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public Date getPublicationDatetime() {
        return publicationDatetime;
    }

    public URI getLocations() {
        return locations;
    }

    public URI getUser() {
        return user;
    }


    public URI getSelf() {
        return self;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public void setPublicationState(PublicationState publicationState) {
        this.publicationState = publicationState;
    }

    public void setPublicationDatetime(Date publicationDatetime) {
        this.publicationDatetime = publicationDatetime;
    }

    public void setLocations(URI locations) {
        this.locations = locations;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
