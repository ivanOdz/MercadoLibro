package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class UserReview {
	
	private long userReviewId;
	private User reviewer;
	private User subject;
	private Exchange exchange;
	private String reviewDescription;
	private Timestamp reviewDate;
	private int reviewRating;
	
	public UserReview(long userReviewId, User reviewer, User subject, Exchange exchange, String reviewDescription, Timestamp reviewDate, int reviewRating) {
		
		this.userReviewId = userReviewId;
		this.reviewer = reviewer;
		this.subject = subject;
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
	
	public User getSubject() {
		
		return subject;
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
