package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class ReviewDTO {
    private String description;
    private Date reviewDate;
    private int rating;

    private URI self;
    private URI subject;
    private URI reviewer;
    private URI exchange;

    static public ReviewDTO fromUserReview(UriInfo uriInfo, UserReview review){
        ReviewDTO dto = new ReviewDTO();
        dto.description = review.getReviewDescription();
        dto.reviewDate = review.getReviewDate() != null ? new Date(review.getReviewDate().getTime()) : null;
        dto.rating = review.getReviewRating();

        dto.reviewer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getReviewer().getUserId())).build();
        dto.subject = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getSubject().getUserId())).build();
        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getSubject().getUserId())).path("reviews").build();
        dto.exchange = uriInfo.getBaseUriBuilder().path("exchanges").path(String.valueOf(review.getExchange().getExchangeId())).build();

        return dto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getSubject() {
        return subject;
    }

    public void setSubject(URI subject) {
        this.subject = subject;
    }

    public URI getReviewer() {
        return reviewer;
    }

    public void setReviewer(URI reviewer) {
        this.reviewer = reviewer;
    }

    public URI getExchange() {
        return exchange;
    }

    public void setExchange(URI exchange) {
        this.exchange = exchange;
    }

    public Long getExchangeId() {
        System.out.println("exchange returned:" + UrnResolverUtil.getExchangeId(exchange));
        return UrnResolverUtil.getExchangeId(exchange);
    }
}
