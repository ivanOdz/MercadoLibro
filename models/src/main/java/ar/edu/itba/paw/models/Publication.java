package ar.edu.itba.paw.models;


import ar.edu.itba.paw.models.utils.PublicationState;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_publicationid_seq")
    @SequenceGenerator(sequenceName = "publication_publicationid_seq", name = "publication_publicationid_seq", allocationSize = 1)
    @Column(name = "publicationid")
    private Long publicationId;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    private PublicationState publicationState;

    @Column(name = "publicationdatetime")
    private Timestamp publicationDatetime;

    @ManyToOne
    @JoinTable(
            name = "publication_location",
            joinColumns = @JoinColumn(name = "publicationid"),
            inverseJoinColumns = @JoinColumn(name = "locationid")
    )
    private Location location;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Formula("(SELECT COUNT(fp.publicationid) FROM favorite_publication fp WHERE fp.publicationid = publicationid)")
    private Integer likes;

    @Formula("(SELECT COUNT(*) > 0 FROM favorite_publication fp WHERE fp.publicationid = publicationid AND fp.userid = userid)")
    private Boolean isLikedByUser;

    public Publication() {
        // only for JPA
    }

    public Publication(Long publicationId, Book book, User user,PublicationState publicationState, Timestamp publicationDatetime, Location location) {
        this.publicationId = publicationId;
        this.book = book;
        this.publicationState = publicationState;
        this.publicationDatetime = publicationDatetime;
        this.location = location;
        this.user = user;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Boolean getLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public PublicationState getPublicationState() {
        return publicationState;
    }

    public void setPublicationState(PublicationState publicationState) {
        this.publicationState = publicationState;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public void setPublicationDatetime(Timestamp publicationDatetime) {
        this.publicationDatetime = publicationDatetime;
    }
    
    public Location getLocation() {
        return location;
    }

    public Integer getLikes() {
        return likes;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setLikedByUser(boolean isLikedByUser) {
        this.isLikedByUser = isLikedByUser;
    }
}
