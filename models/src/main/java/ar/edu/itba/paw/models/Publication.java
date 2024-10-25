package ar.edu.itba.paw.models;


import ar.edu.itba.paw.models.utils.PublicationState;

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

    // No me acuerdo si se podian elegir las location o no. Segun eso poner el OneToMany o OneToOne.

    @ManyToMany
    @JoinTable(
            name = "publication_location",
            joinColumns = @JoinColumn(name = "publicationid"),
            inverseJoinColumns = @JoinColumn(name = "locationid")
    )
    private Set<Location> locations;


    public Publication(Long publicationId, Book book, PublicationState publicationState, Timestamp publicationDatetime, Set<Location> locations) {
        this.publicationId = publicationId;
        this.book = book;
        this.publicationState = publicationState;
        this.publicationDatetime = publicationDatetime;
        this.locations = locations;
    }

    public Publication() {

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

    Set<Location> getLocation() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }
}
