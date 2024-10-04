package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class UserReview {
	
	private long userReviewId;
	private User reviewer;
	private Exchange exchange;
	private String reviewDescription;
	private Timestamp reviewDate;
	private int reviewRating;
	
	public UserReview(long userReviewId, User reviewer, Exchange exchange, String reviewDescription, Timestamp reviewDate, int reviewRating) {
		
		this.userReviewId = userReviewId;
		this.reviewer = reviewer;
		this.exchange = exchange;
		this.reviewDescription = reviewDescription;
		this.reviewDate = reviewDate;
		this.reviewRating = reviewRating;
    }
	
	public long getUserReviewId() {
		
		return userReviewId;
	}
	
	public User getReviewer() {
		
		return reviewer;
	}
	
	public Exchange getExchange() {
		
		return exchange;
	}
	
	public String getReviewDescription() {
		
		return reviewDescription;
	}
	
	public Timestamp getReviewDate() {
		
		return reviewDate;
	}
	
	public int getReviewRating() {
		
		return reviewRating;
	}
}
