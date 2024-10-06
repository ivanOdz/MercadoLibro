package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
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
	public Boolean createUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating) {
		
		// VALIDAR!
		return urDao.createUserReview(exchangeId, userId, userSubjectId, description, rating);
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
