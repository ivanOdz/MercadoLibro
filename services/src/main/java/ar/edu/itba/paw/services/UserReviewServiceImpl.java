package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

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
    
    @Override
    public int getUserRating(long userId) {
    	
    	int totalRating = 0;
    	int reviewCount = 0;
    	int porcentage = 0;
    	
    	List<UserReview> reviews = getReviewsByUserId(userId); 
    	
        if (reviews.isEmpty()) {
            return porcentage;
        }
        
    	for (UserReview review : reviews) {
    		
    		totalRating += review.getUserReviewRating();
    		reviewCount++;
    	}
    	
    	porcentage = totalRating / reviewCount;
    	
    	return porcentage;
    }
    
    @Override
    public Boolean createUserReview(UserReview userReview) {
    	
    	return urDao.createUserReview(userReview);
    }
}
