package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.UserReview;

import java.util.List;

public interface UserReviewDao {
	
	Boolean createUserReview(long exchangeId, long userId, String description, int rating);
	
	UserReview getUserReview(long exchangeId, long userId);
	
	List<UserReview> getReviewsGivenByUserId(long userId);
	
	List<UserReview> getReviewsEarnedByUserId(long userId);
	
	int getUserAverageRatingEarned(long userId);
	
	int getUserAverageRatingGiven(long userId);
	
	boolean isReviewable(long exchangeId, long userId);
}
