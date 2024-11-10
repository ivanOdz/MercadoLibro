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
	private Long userReviewId;

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

	@Column(name = "userReviewRating")
	private int reviewRating;

	public UserReview() {
		// Only for JPA
	}

	public UserReview(Long userReviewId, User reviewer, User subject, Exchange exchange, String reviewDescription, Timestamp reviewDate, int reviewRating) {
		
		this.userReviewId = userReviewId;
		this.reviewer = reviewer;
		this.subject = subject;
		this.exchange = exchange;
		this.reviewDescription = reviewDescription;
		this.reviewDate = reviewDate;
		this.reviewRating = reviewRating;
    }
	
	public Long getUserReviewId() {
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

	public void setReviewRating(int reviewRating) {
		this.reviewRating = reviewRating;
	}

	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}

	public void setReviewDescription(String reviewDescription) {
		this.reviewDescription = reviewDescription;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public void setSubject(User subject) {
		this.subject = subject;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public void setUserReviewId(Long userReviewId) {
		this.userReviewId = userReviewId;
	}
}
