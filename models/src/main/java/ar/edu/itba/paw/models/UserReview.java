package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_review")
public class UserReview {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_review_userreviewid_seq")
	@SequenceGenerator(sequenceName = "user_review_userreviewid_seq", name = "user_review_userreviewid_seq", allocationSize = 1)
	@Column(name = "userReviewId")
	private long userReviewId;

	@ManyToOne
	@JoinColumn(name = "reviewerId", referencedColumnName = "userId")
	private User reviewer;

	@ManyToOne
	@JoinColumn(name = "subjectId", referencedColumnName = "userId")
	private User subject;

	@ManyToOne
	@JoinColumn(name = "exchangeId", referencedColumnName = "exchangeId")
	private Exchange exchange;

	@Column(name = "reviewDescription")
	private String reviewDescription;

	@Column(name = "reviewDate")
	private Timestamp reviewDate;

	@Column(name = "reviewRating")
	private int reviewRating;

	public UserReview() {
		// Only for JPA
	}

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
