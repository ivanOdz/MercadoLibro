package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.util.Optional;

public interface UserReviewDao {

	UserReview createOrUpdateUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage);

	Optional<UserReview> getUserReviewEarned(long exchangeId, long userId);

	Optional<UserReview> getUserReviewGiven(long exchangeId, long userId);
	
	Optional<Rating> getUserRatingEarned(long userId);

	Optional<Rating> getUserRatingGiven(long userId);
}
