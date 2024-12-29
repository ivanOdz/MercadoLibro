package ar.edu.itba.paw.webapp.dto.PublicationDTO;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;

public class PublicationDTO {
    private URI book;
    private PublicationState publicationState;
    private Timestamp publicationDatetime;
    private URI locations; //maybe List<LocationDTO> ?
    private URI user;
    private Integer likes;
    private Boolean isLikedByUser;

    private URI self;


    public PublicationDTO fromPublication(final UriInfo uriInfo, final Publication publication) {
        final PublicationDTO dto = new PublicationDTO();

        dto.publicationState = publication.getPublicationState();
        dto.publicationDatetime = publication.getPublicationDatetime();
        dto.likes = publication.getLikes();
        dto.isLikedByUser = publication.getLikedByUser();

        dto.self = uriInfo.getBaseUriBuilder().path("publications").path(String.valueOf(publication.getPublicationId())).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(publication.getBook().getBookId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(publication.getUser())).build();

        // TODO : locations

        return dto;
    }

    public URI getBook() {
        return book;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public URI getLocations() {
        return locations;
    }

    public URI getUser() {
        return user;
    }

    public Integer getLikes() {
        return likes;
    }

    public Boolean getLikedByUser() {
        return isLikedByUser;
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

    public void setPublicationDatetime(Timestamp publicationDatetime) {
        this.publicationDatetime = publicationDatetime;
    }

    public void setLocations(URI locations) {
        this.locations = locations;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setLikedByUser(Boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
