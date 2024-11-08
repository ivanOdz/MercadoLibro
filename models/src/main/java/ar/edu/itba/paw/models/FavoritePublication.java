package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "favorite_publication")
public class FavoritePublication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "favorite_publication_favoritepublicationid_seq")
    @SequenceGenerator(sequenceName = "favorite_publication_favoritepublicationid_seq", name = "favorite_publication_favoritepublicationid_seq", allocationSize = 1)
    @Column(name = "favoritepublicationid")
    private Long favoritepublicationId;

    @ManyToOne
    @JoinColumn(name = "publicationid", referencedColumnName = "publicationId", nullable = false)
    private Publication publication;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column(name = "liked_at")
    private Timestamp likedAt;


    public FavoritePublication() {
        // Hibernate
    }

    public FavoritePublication(Publication publication, User user) {
        this.publication = publication;
        this.user = user;
    }

    public Long getFavoritepublicationId() {
        return favoritepublicationId;
    }

    public Publication getPublication() {
        return publication;
    }

    public User getUser() {
        return user;
    }

    public void setFavoritepublicationId(Long favoritepublicationId) {
        this.favoritepublicationId = favoritepublicationId;
    }

    public void setPublication(Publication publicationId) {
        this.publication = publicationId;
    }

    public void setUser(User userId) {
        this.user = userId;
    }
}
