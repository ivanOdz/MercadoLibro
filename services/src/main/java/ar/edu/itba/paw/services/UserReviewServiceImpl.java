package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.stereotype.Service;

@Service
public class UserReviewServiceImpl implements UserReviewService {

	private final UserReviewDao urDao;
	private final ExchangeService exchangeService;
	
	public UserReviewServiceImpl(final UserReviewDao urDao, final ExchangeService exchangeService) {
		
		this.urDao = urDao;
		this.exchangeService = exchangeService;
	}

	@Override
	public Boolean createUserReview(long exchangeId, long userId, String description, int rating) {
		
		Exchange exchange = exchangeService.getExchangeById(exchangeId).get();
		
		if (exchange != null && exchange.getIsReviewable() == true) {
			
			long offererId = exchange.getOfferer().getBook().getOwner().getUserId();
			long requesterId = exchange.getRequester().getBook().getOwner().getUserId();
			long subjectId = offererId != userId ? offererId : requesterId;
			
			return urDao.createUserReview(exchangeId, userId, subjectId, description, rating);
		}
		
		return false;
	}
	
	@Override
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId) {

		return urDao.getReviewsGivenByUserId(userId);
	}

	@Override
	public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId) {

		return urDao.getReviewsEarnedByUserId(userId);
	}
	
	@Override
	public UserReview getUserReviewEarned(long exchangeId, long userId) {
		
		return urDao.getUserReviewEarned(exchangeId, userId);
	}
	
	@Override
	public UserReview getUserReviewGiven(long exchangeId, long userId) {
		
		return urDao.getUserReviewGiven(exchangeId, userId);
	}
	
	@Override
	public Rating getUserRatingEarned(long userId) {
		
		return urDao.getUserRatingEarned(userId);
	}
    
	@Override
	public Rating getUserRatingGiven(long userId) {
		
		return urDao.getUserRatingGiven(userId);
	}
}
