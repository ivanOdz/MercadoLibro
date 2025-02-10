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
    private URI favoriteEndpoint; // /{publication_id}/favorite -> for POST purposes

    private String isFavoriteTemplate; // /{publication_id}/favorite?user_id={user_id} -> for checking if a publication is favorite for a specific user

    private URI self;


    public static PublicationDTO fromPublication(final UriInfo uriInfo, final Publication publication) {
        final PublicationDTO dto = new PublicationDTO();

        dto.publicationState = publication.getPublicationState();
        dto.publicationDatetime = publication.getPublicationDatetime();

        dto.self = uriInfo.getBaseUriBuilder().path("publications").path(String.valueOf(publication.getPublicationId())).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(publication.getBook().getBookId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(publication.getUser().getUserId())).build();

        dto.locations = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(publication.getUser().getUserId())).path("locations").queryParam("publication_id", publication.getPublicationId()).build();
        dto.favoriteEndpoint = uriInfo.getBaseUriBuilder().path("publications").path(String.valueOf(publication.getPublicationId())).path("favorite").build();

        dto.isFavoriteTemplate = uriInfo.getBaseUriBuilder()
                .path("publications")
                .path(String.valueOf(publication.getPublicationId()))
                .path("favorite")
                .toTemplate() + "?user_id={user_id}";
        return dto;
    }

    public String getIsFavoriteTemplate() {
        return isFavoriteTemplate;
    }

    public void setIsFavoriteTemplate(String isFavoriteTemplate) {
        this.isFavoriteTemplate = isFavoriteTemplate;
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
