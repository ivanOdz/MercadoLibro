package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Publication> publications = new ArrayList<>();
    
    @OneToMany(mappedBy = "locations")
    private List<User> users = new ArrayList<>();
    
    public Location() {
        // Hibernate
    }

    public Location(Long locationId, String locationString) {
        this.locationId = locationId;
        this.locationString = locationString;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public Long getLocationId() {
        return locationId;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public List<User> getUsers() {
        return users;
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
    
    public void addUser(User user) {
        users.add(user);
    }
}
