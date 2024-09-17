package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReviewServiceImpl implements UserReviewService {
    UserReviewDao urDao;

    public UserReviewServiceImpl(UserReviewDao urDao) {
        this.urDao = urDao;
    }

    @Override
    public List<UserReview> getReviewsByUserId(long userId) {
        return urDao.getReviewsByUserId(userId);
    }
    
    @Override
    public UserReview getUserReview(long exchangeId, long reviewerId) {
    	return urDao.getUserReview(exchangeId, reviewerId);
    }
}
