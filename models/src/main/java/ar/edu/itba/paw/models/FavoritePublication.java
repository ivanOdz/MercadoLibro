package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "favorite_publication")
public class FavoritePublication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "favorite_publication_favoritepublicationid_seq")
    @SequenceGenerator(sequenceName = "favorite_publication_favoritepublicationid_seq", name = "favorite_publication_favoritepublicationid_seq", allocationSize = 1)
    @Column(name = "favoritepublicationid")
    private Long favoritepublicationId;

    @Column(name = "publicationid")
    private Long publicationId;

    @Column(name = "userid")
    private Long userId;

    public FavoritePublication() {
        // Hibernate
    }

    public FavoritePublication(Long publicationId, Long userId) {
        this.publicationId = publicationId;
        this.userId = userId;
    }

    public Long getFavoritepublicationId() {
        return favoritepublicationId;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setFavoritepublicationId(Long favoritepublicationId) {
        this.favoritepublicationId = favoritepublicationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
