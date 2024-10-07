package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

public interface UserReviewService {

	Boolean createUserReview(long exchangeId, long userId, String description, int rating);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage);

	UserReview getUserReviewEarned(long exchangeId, long userId);
	
	UserReview getUserReviewGiven(long exchangeId, long userId);
	
	Rating getUserRatingEarned(long userId);
	
	Rating getUserRatingGiven(long userId);
	
//	public Boolean isReviewable(long exchangeId, long userId);
}
