package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserReviewService {
	
    List<UserReview> getReviewsByUserId(long userId);
    
    UserReview getUserReview(long exchangeId, long reviewerId);
    
    int getUserRating(long userId);
    
    Boolean createUserReview(UserReview userReview);
}
