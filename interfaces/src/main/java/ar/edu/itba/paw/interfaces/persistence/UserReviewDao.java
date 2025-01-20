package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.util.Optional;

public interface UserReviewDao {

	UserReview findUserReviewById(long reviewId);

	UserReview createOrUpdateUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating);

	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage);

	Optional<Rating> getUserRatingEarned(long userId);

}
