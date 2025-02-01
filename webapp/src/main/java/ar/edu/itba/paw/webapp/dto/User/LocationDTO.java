package ar.edu.itba.paw.webapp.dto.User;

import ar.edu.itba.paw.models.Location;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LocationDTO {

    private String location;

    private URI self;

    private URI publications;  // GET -> publications?locationId=3

    public static LocationDTO fromLocation(UriInfo uriInfo, long userId, Location location) {
        LocationDTO dto = new LocationDTO();

        dto.location = location.getLocationString();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(Long.toString(userId)).path("locations").path(Long.toString(location.getLocationId())).build();
        dto.publications =  uriInfo.getBaseUriBuilder().path("publications").queryParam("locationId", location.getLocationId()).build();

        return dto;
    }

    public URI getPublications() {
        return publications;
    }

    public void setPublications(URI publications) {
        this.publications = publications;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
