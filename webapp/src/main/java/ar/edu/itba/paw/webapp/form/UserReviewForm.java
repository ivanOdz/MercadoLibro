package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserReviewForm {

	@NotNull
    private long exchangeId;

    @NotNull
    private int userReviewRating;

    @NotNull
    @Size(min = 1, max = 255)
    private String reviewDescription;

    @NotNull
    private long offererPubId;

    @NotNull
    private long requesterPubId;

    public void setExchangeId(@NotNull long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void setUserReviewRating(@NotNull int userReviewRating) {
        this.userReviewRating = userReviewRating;
    }

    public void setReviewDescription(@NotNull @Size(min = 1, max = 255) String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public void setOffererPubId(@NotNull long offererPubId) {
        this.offererPubId = offererPubId;
    }

    public void setRequesterPubId(@NotNull long requesterPubId) {
        this.requesterPubId = requesterPubId;
    }

    @NotNull
    public long getExchangeId() {
        return exchangeId;
    }

    @NotNull
    public int getUserReviewRating() {
        return userReviewRating;
    }

    public @NotNull @Size(min = 1, max = 255) String getReviewDescription() {
        return reviewDescription;
    }

    @NotNull
    public long getOffererPubId() {
        return offererPubId;
    }

    @NotNull
    public long getRequesterPubId() {
        return requesterPubId;
    }
}
