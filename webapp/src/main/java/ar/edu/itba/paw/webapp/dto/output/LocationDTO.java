package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Location;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LocationDTO {

    private String location;

    private URI self;

    static public LocationDTO fromLocation(UriInfo uriInfo, long userId, Location location) {
        LocationDTO dto = new LocationDTO();

        dto.location = location.getLocationString();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(Long.toString(userId)).path("locations").path(Long.toString(location.getLocationId())).build();

        return dto;
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
