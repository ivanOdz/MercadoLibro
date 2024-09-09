package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class UserReview {
    private final long userReviewId;
    private final long exchangeId;
    private final long reviewerId;
    private final long subjectId;
    private final String reviewDescription;
    private final Timestamp reviewDate;
    private final int userReviewRating;

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
}
