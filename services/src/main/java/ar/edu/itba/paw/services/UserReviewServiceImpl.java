package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.UserRatingNotFound;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class UserReviewServiceImpl implements UserReviewService {

	@Autowired
	private UserReviewDao userReviewDao;

	@Autowired
	private ExchangeService exchangeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserReviewServiceImpl.class);

	@Override
	public UserReview findUserReviewById(long reviewId) {
		return userReviewDao.findUserReviewById(reviewId);
	}

	@Override
	@Transactional
	public UserReview createUserReview(long exchangeId, Long targetUserId, String description, int rating) {
		Exchange exchange = exchangeService.getExchangeById(exchangeId);

		if(targetUserId == null) {
			LOGGER.warn("The provided userId: {} cannot be null", targetUserId);
			return null;
		}

		long offererId = exchange.getOfferer().getUser().getUserId();
		long requesterId = exchange.getRequester().getUser().getUserId();

		if (targetUserId != offererId && targetUserId != requesterId) {
			LOGGER.warn("The provided userId: {} does not match the offerer or requester for exchange ID: {}", targetUserId, exchangeId);
			return null;
		}

		long reviewerId = (targetUserId != offererId) ? offererId : requesterId;

		LOGGER.info("Creating or updating user review for exchange ID: {} and user ID: {}", exchangeId, targetUserId);
		UserReview userReview = userReviewDao.createOrUpdateUserReview(exchangeId, reviewerId, targetUserId, description, rating);

		LOGGER.info("User review created/updated for user ID: {} on exchange ID: {}", targetUserId, exchangeId);

		return userReview;
	}

    @Override
	@Transactional(readOnly = true)
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsEarnedByUserId(userId, currentPage);
	}

    @Override
	@Transactional(readOnly = true)
	public Rating getUserRatingEarned(long userId) {
		LOGGER.info("Fetching user rating earned for user ID: {}", userId);

		Optional<Rating> rating = userReviewDao.getUserRatingEarned(userId);
		if(rating.isEmpty()) {
			LOGGER.warn("User rating not found for user ID: {}", userId);
			throw new UserRatingNotFound("Error getting user rating earned.");
		}

		LOGGER.info("User rating found for user ID: {}", userId);
		return rating.get();
	}
}
