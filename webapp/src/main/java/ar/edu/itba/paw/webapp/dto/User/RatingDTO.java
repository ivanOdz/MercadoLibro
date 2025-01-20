package ar.edu.itba.paw.webapp.dto.User;

import ar.edu.itba.paw.models.utils.Rating;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class RatingDTO {

    private double average;

    private int count;

    private URI self;

    public static RatingDTO fromRating(final UriInfo uriInfo, final long userId, final Rating rating) {

        final RatingDTO dto = new RatingDTO();

        dto.average = rating.getRating();
        dto.count = rating.getRatingCount();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(userId)).path("rating").build();

        return dto;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

