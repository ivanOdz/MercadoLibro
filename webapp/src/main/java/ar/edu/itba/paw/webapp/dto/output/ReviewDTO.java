package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.UserReview;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;

public class ReviewDTO {
    private String description;
    private Timestamp reviewDate;
    private int rating;

    private URI self;
    private URI subject;
    private URI reviewer;
    private URI exchange;

    static public ReviewDTO fromUserReview(UriInfo uriInfo, UserReview review){
        ReviewDTO dto = new ReviewDTO();
        dto.description = review.getReviewDescription();
        dto.reviewDate = review.getReviewDate();
        dto.rating = review.getReviewRating();

        dto.reviewer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getReviewer().getUserId())).build();
        dto.subject = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getSubject().getUserId())).build();
        dto.self = uriInfo.getBaseUriBuilder().path("user").path(String.valueOf(review.getSubject().getUserId())).path("reviews").build();

        return dto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
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
}
