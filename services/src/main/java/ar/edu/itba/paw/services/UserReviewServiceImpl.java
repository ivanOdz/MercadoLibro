package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

import java.util.List;

@Service
public class UserReviewServiceImpl implements UserReviewService {

	private final UserReviewDao urDao;

	public UserReviewServiceImpl(UserReviewDao urDao) {
		
		this.urDao = urDao;
	}

	@Override
	public Boolean createUserReview(long exchangeId, long userId, String description, int rating) {
		return urDao.createUserReview(exchangeId, userId, description, rating);
	}
	
	@Override
	public UserReview getUserReview(long exchangeId, long reviewerId) {
		
		return urDao.getUserReview(exchangeId, reviewerId);
	}
	
	@Override
	public int getUserAverageRatingEarned(long userId) {
		
		return urDao.getUserAverageRatingEarned(userId);
	}
	
	@Override
	public PaginatedResponse<UserReview> getReviewsGivenByUserId(long userId) {

		return urDao.getReviewsGivenByUserId(userId);
	}

	@Override
	public PaginatedResponse<UserReview> getReviewsEarnedByUserId(long userId) {

		return urDao.getReviewsEarnedByUserId(userId);
	}

	@Override
	public int getUserAverageRatingGiven(long userId) {
		
		return urDao.getUserAverageRatingGiven(userId);
	}
    
	@Override
    public boolean isReviewable(long exchangeId, long userId) {
    	
    	return urDao.isReviewable(exchangeId, userId);
    }
}
