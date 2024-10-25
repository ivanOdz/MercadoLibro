package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_locationid_seq")
    @SequenceGenerator(sequenceName = "location_locationid_seq", name = "location_locationid_seq", allocationSize = 1)
    @Column(name = "locationid")
    private Long locationId;

    private String locationString;

    @ManyToMany(mappedBy = "locations")
    private Set<Publication> publications = new HashSet<>();

    public Location() {
        // Hibernate
    }

    public Location(Long locationId, String locationString) {
        this.locationId = locationId;
        this.locationString = locationString;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
    }
}
