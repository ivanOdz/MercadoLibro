package ar.edu.itba.paw.webapp.dto.output;

import java.net.URI;

import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

public class UserDTO {
	
	private String username;
	
	private String mail;
	
	private int ratingCount;

	private double ratingAverage;

	private URI self;

	//private URI favoriteLocation;

	private URI locations;

	private URI reviews;

	private URI books;

	private URI favorites;

	private URI exchanges;


	public static UserDTO fromUser(final UriInfo uriInfo, final User user, Rating userRating) {
		
		final UserDTO dto = new UserDTO();
		
		dto.username = user.getUsername();
		dto.mail = user.getMail();
		dto.ratingCount = userRating.getRatingCount();
		dto.ratingAverage = userRating.getRating();

		// links
		dto.self = URI.create("/users/" + user.getUserId());
		dto.locations = URI.create("/users/" + user.getUserId() + "/locations");
		//dto.favoriteLocation = URI.create("/users/" + user.getUserId() + "/locations/" + user.getFavoriteLocation().getLocationId());
		dto.reviews = URI.create("/users/" + user.getUserId() + "/reviews");
		dto.books = URI.create("/books?owner=" + user.getUserId());
		dto.favorites = URI.create("/users/" + user.getUserId() + "/favorite");
		dto.exchanges = URI.create("/exchanges?user_id=" + user.getUserId());
		return dto;
	}

	public URI getFavorites() {
		return favorites;
	}

	public void setFavorites(URI favorites) {
		this.favorites = favorites;
	}


	public URI getReviews() {
		return reviews;
	}

	public void setReviews(URI reviews) {
		this.reviews = reviews;
	}

	public URI getBooks() {
		return books;
	}

	public void setBooks(URI books) {
		this.books = books;
	}

	public void setLocations(URI locations) {
		this.locations = locations;
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

    public void setLocation(URI locations) {
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

	public Long getSelfId() {
		return UrnResolverUtil.getUserId(self);
	}


    public URI getLocations() {
    	return locations;
    }

	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public double getRatingAverage() {
		return ratingAverage;
	}

	public void setRatingAverage(double ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	public URI getExchanges() {
		return exchanges;
	}

	public void setExchanges(URI exchanges) {
		this.exchanges = exchanges;
	}
}

