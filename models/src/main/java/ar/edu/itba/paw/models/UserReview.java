package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class UserReview {
	
    private long userReviewId;
    private long exchangeId;
    private long reviewerId;
    private long subjectId;
    private String reviewDescription;
    private Timestamp reviewDate;
    private int userReviewRating;

    public UserReview() { }
    
    public UserReview(long userReviewId, long exchangeId, long reviewerId, long subjectId,
                      String reviewDescription, Timestamp reviewDate, int userReviewRating) {
        this.userReviewId = userReviewId;
        this.exchangeId = exchangeId;
        this.reviewerId = reviewerId;
        this.subjectId = subjectId;
        this.reviewDescription = reviewDescription;
        this.reviewDate = reviewDate;
        this.userReviewRating = userReviewRating;
    }

    public long getUserReviewId() {
        return userReviewId;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public int getUserReviewRating() {
        return userReviewRating;
    }
    
    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setRating(int userReviewRating) {
        this.userReviewRating = userReviewRating;
    }
}
