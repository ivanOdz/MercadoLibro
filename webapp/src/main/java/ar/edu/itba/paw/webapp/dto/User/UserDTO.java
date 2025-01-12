package ar.edu.itba.paw.webapp.dto.User;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.models.User;

public class UserDTO {
	
	private String username;
	
	private String mail;
	
	private URI self;
	
	private URI image;
	
	private URI favoriteLocation;
	
	private List<URI> locations;
	
	
	public static UserDTO fromUser(final UriInfo uriInfo, final User user) {
		
		final UserDTO dto = new UserDTO();
		
		dto.username = user.getUsername();
		dto.mail = user.getMail();
		
		dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getUserId())).build();
		dto.image = uriInfo.getBaseUriBuilder().path("image").path(String.valueOf(user.getImageId())).build();
		dto.favoriteLocation = uriInfo.getBaseUriBuilder().path("location").path(String.valueOf(user.getFavoriteLocation().getLocationId())).build();
		dto.locations = user.getUserLocations().stream().map(location -> uriInfo.getBaseUriBuilder().path("location").path(String.valueOf(location.getLocationId())).build()).toList();
		
		return dto;
	}
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setMail(String mail) {
    	this.mail = mail;
    }
    
    public void setSelf(URI self) {
        this.self = self;
    }
	
    public void setImage(URI image) {
    	this.image = image;
    }
    
    public void setFavoriteLocation(URI favoriteLocation) {
    	this.favoriteLocation = favoriteLocation;
    }
    
    public void setLocation(List<URI> locations) {
    	this.locations = locations;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public String getMail() {
    	return mail;
    }
    
    public URI getSelf() {
    	return self;
    }
    
    public URI getImage() {
    	return image;
    }
    
    public URI getFavoriteLocation() {
    	return favoriteLocation;
    }
    
    public List<URI> getLocations() {
    	return locations;
    }
}
