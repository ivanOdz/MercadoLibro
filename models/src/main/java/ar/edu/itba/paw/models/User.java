package ar.edu.itba.paw.models;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_userid_seq")
    @SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
    @Column(name = "userid")
    private Long userId;

    @Column(length = 64, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, insertable = true, updatable = true)
    private String password;

    @Column(name = "imageid")
    private Long imageId;

    @Column(name = "verificationcode")
    private Integer verificationCode;

    @Column(name = "isverified")
    private boolean isVerified;

    @Column(length = 64, nullable = false)
    private String language;

    private String mail;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    
    @ManyToOne
    @JoinColumn(name = "favoriteLocation", referencedColumnName = "locationId")
    private Location favoriteLocation;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_location",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "locationid")
    )
    private Set<Location> userLocations = new HashSet<>();

    /* package */ public User(){

        // only for JPA
    }
    
    public User(Long userId, String username, String mail, String password, Long imageId, Integer verificationCode, boolean isVerified, String language) {
    	
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.imageId = imageId;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.language = language;
    }


    public long getUserId() {
        return userId;
    }

    public String getLanguage() {
        return language;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        // TODO:
        return password;
    }

    public Long getImageId() {
        return imageId;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public boolean isVerified() {
        return isVerified;
    }
    
    public String getFavoriteLocation() {
    	if (favoriteLocation != null) {
    		return favoriteLocation.getLocationString();
    	}
    	return null;
    }
    
    public Set<Location> getUserLocations() {
    	return userLocations;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public void setFavoriteLocation(Location favoriteLocation) {
    	this.favoriteLocation = favoriteLocation;
    }

    public void removeLocation(Location location) {
    	userLocations.removeIf(userLocation -> userLocation.getLocationString().equals(location.getLocationString()));	// Revisar*
    }
    
    public void addLocation(Location location) {
    	userLocations.add(location);
    }
}
