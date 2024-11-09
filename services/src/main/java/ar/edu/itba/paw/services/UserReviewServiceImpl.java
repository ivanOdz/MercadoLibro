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
	public void createUserReview(long exchangeId, long userId, String description, int rating) {
		Exchange exchange = exchangeService.getExchangeById(exchangeId);

		boolean nin = exchange.getIsReviewable();
		boolean nout = getUserReviewGiven(exchangeId, userId) == null;

		long offererId = exchange.getOfferer().getBook().getOwner().getUserId();
		long requesterId = exchange.getRequester().getBook().getOwner().getUserId();
		long subjectId = offererId != userId ? offererId : requesterId;
		userReviewDao.createUserReview(exchangeId, userId, subjectId, description, rating);
	}

    @Override
	@Transactional(readOnly = true)
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsGivenByUserId(userId, currentPage);
	}

    @Override
	@Transactional(readOnly = true)
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsEarnedByUserId(userId, currentPage);
	}

    @Override
	@Transactional(readOnly = true)
	public UserReview getUserReviewEarned(long exchangeId, long userId) {
		Optional<UserReview> ur = userReviewDao.getUserReviewEarned(exchangeId, userId);

		if(ur.isEmpty()) {
			throw new UserReviewNotFound("Error getting user review earned.");
		}
		return ur.get();
	}


    @Override
	@Transactional(readOnly = true)
	public UserReview getUserReviewGiven(long exchangeId, long userId) {
		Optional<UserReview> ur = userReviewDao.getUserReviewGiven(exchangeId, userId);

		if(ur.isEmpty()) {
			throw new UserReviewNotFound("Error getting user review given.");
		}
		return ur.get();
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

    @Override
	@Transactional(readOnly = true)
	public Rating getUserRatingGiven(long userId) {
		Optional<Rating> rating = userReviewDao.getUserRatingGiven(userId);
		if(rating.isEmpty()) {
			throw new UserRatingNotFound("Error getting user rating given.");
		}

		return rating.get();
	}

}
