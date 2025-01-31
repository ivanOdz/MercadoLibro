package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.net.URI;


public interface UserReviewService {

	UserReview findUserReviewById(long targetId, long reviewId);

	UserReview createUserReview(URI exchangeUrn, Long targetId, String description, int rating);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage);
	
	Rating getUserRatingEarned(long userId);

}
