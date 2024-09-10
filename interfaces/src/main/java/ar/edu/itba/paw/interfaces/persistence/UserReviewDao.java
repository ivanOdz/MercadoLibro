package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.UserReview;

import java.util.List;

public interface UserReviewDao {
    List<UserReview> getReviewsByUserId(long userId);
}
