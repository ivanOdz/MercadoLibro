package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.UserRatingNotFound;
import ar.edu.itba.paw.interfaces.exceptions.UserReviewNotFound;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
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

	@Override
	@Transactional
	public UserReview createUserReview(long exchangeId, long userId, String description, int rating) {
		Exchange exchange = exchangeService.getExchangeById(exchangeId);
		return userReviewDao.createOrUpdateUserReview(exchangeId, userId, userId != exchange.getOfferer().getUser().getUserId() ?
				exchange.getOfferer().getUser().getUserId() : exchange.getRequester().getUser().getUserId(), description, rating);
	}

    @Override
	@Transactional(readOnly = true)
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsEarnedByUserId(userId, currentPage);
	}

    @Override
	@Transactional(readOnly = true)
	public Rating getUserRatingEarned(long userId) {
		Optional<Rating> rating = userReviewDao.getUserRatingEarned(userId);
		if(rating.isEmpty()) {
			throw new UserRatingNotFound("Error getting user rating earned.");
		}
		return rating.get();
	}
}
