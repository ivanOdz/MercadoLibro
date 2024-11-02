package ar.edu.itba.paw.services;

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

@Service
public class UserReviewServiceImpl implements UserReviewService {

	@Autowired
	private UserReviewDao userReviewDao;

	@Autowired
	private ExchangeService exchangeService;


	@Override
	public Boolean createUserReview(long exchangeId, long userId, String description, int rating) {
		// NOTE: throws NF exception
		Exchange exchange = exchangeService.getExchangeById(exchangeId);

		boolean nin = exchange.getIsReviewable();
		boolean nout = getUserReviewGiven(exchangeId, userId) == null;

		long offererId = exchange.getOfferer().getBook().getOwner().getUserId();
		long requesterId = exchange.getRequester().getBook().getOwner().getUserId();
		long subjectId = offererId != userId ? offererId : requesterId;

		// IMPLEMENT: exception
		// NOTE: throws a BR exception
		return userReviewDao.createUserReview(exchangeId, userId, subjectId, description, rating) != null;
	}
	
	@Override
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsGivenByUserId(userId, currentPage);
	}

	@Override
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
		return userReviewDao.getReviewsEarnedByUserId(userId, currentPage);
	}
	
	@Override
	public UserReview getUserReviewEarned(long exchangeId, long userId) {
		return userReviewDao.getUserReviewEarned(exchangeId, userId);
	}
	
	@Override
	public UserReview getUserReviewGiven(long exchangeId, long userId) {
		return userReviewDao.getUserReviewGiven(exchangeId, userId);
	}
	
	@Override
	public Rating getUserRatingEarned(long userId) {
		return userReviewDao.getUserRatingEarned(userId);
	}
    
	@Override
	public Rating getUserRatingGiven(long userId) {
		return userReviewDao.getUserRatingGiven(userId);
	}

}
