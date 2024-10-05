package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.util.List;

public interface UserReviewDao {

	Boolean createUserReview(long exchangeId, long userId, String description, int rating);
	
	UserReview getUserReview(long exchangeId, long userId);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId);

	int getUserAverageRatingEarned(long userId);
	
	int getUserAverageRatingGiven(long userId);
	
	boolean isReviewable(long exchangeId, long userId);
}
