package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserReviewService {
	
    List<UserReview> getReviewsByUserId(long userId);
    
    Boolean createUserReview(long exchangeId, long userId, String description, int rating);
    
    UserReview getUserReview(long exchangeId, long reviewerId);
    
    int getUserAverageRatingEarned(long userId);
    
    int getUserAverageRatingGiven(long userId);
    
    boolean isReviewable(long exchangeId, long userId);
}
