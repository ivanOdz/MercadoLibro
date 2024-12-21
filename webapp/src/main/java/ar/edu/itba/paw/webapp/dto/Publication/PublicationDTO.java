package ar.edu.itba.paw.webapp.dto.Publication;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

public class PublicationDTO {
    private Long publicationId;
    private Book book;
    private PublicationState publicationState;
    private Timestamp publicationDatetime;
    private List<Location> locations;
    private User user;
    private Integer likes;
    private Boolean isLikedByUser = false;

    private URI self;

    public Long getPublicationId() {
        return publicationId;
    }

    public Book getBook() {
        return book;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public User getUser() {
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

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setPublicationState(PublicationState publicationState) {
        this.publicationState = publicationState;
    }

    public void setPublicationDatetime(Timestamp publicationDatetime) {
        this.publicationDatetime = publicationDatetime;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setUser(User user) {
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
