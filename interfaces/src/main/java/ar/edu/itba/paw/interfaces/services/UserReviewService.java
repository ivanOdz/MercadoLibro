package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserReviewService {
    
    Boolean createUserReview(long exchangeId, long userId, String description, int rating);
    
    UserReview getUserReview(long exchangeId, long reviewerId);

    PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId);

    PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId);

    int getUserAverageRatingEarned(long userId);
    
    int getUserAverageRatingGiven(long userId);
    
    boolean isReviewable(long exchangeId, long userId);
}
