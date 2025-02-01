package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;
import org.glassfish.jersey.server.Uri;

import javax.ws.rs.QueryParam;
import java.net.URI;

public class ReviewInputDTO {
    private String description;
    private int rating;

    private URI exchangeUrn;

    public Long getExchangeUrn() {
        return UrnResolverUtil.getExchangeId(exchangeUrn);
    }

    public void setExchangeUrn(URI exchangeUrn) {
        this.exchangeUrn = exchangeUrn;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
