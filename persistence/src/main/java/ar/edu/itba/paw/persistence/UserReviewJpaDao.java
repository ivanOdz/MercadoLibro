package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;

@Primary
@Repository
public class UserReviewJpaDao implements UserReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserReview createUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating) {
        final UserReview userReview = new UserReview(null, em.find(User.class, userId), em.find(User.class, userSubjectId), em.find(Exchange.class, exchangeId), description, new Timestamp(new Date().getTime()), rating);
        em.persist(userReview);
        return userReview;
    }

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage) {
        return null;
    }

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
        return null;
    }

    @Override
    public UserReview getUserReviewEarned(long exchangeId, long userId) {
        return null;
    }

    @Override
    public UserReview getUserReviewGiven(long exchangeId, long userId) {
        return null;
    }

    @Override
    public Rating getUserRatingEarned(long userId) {
        return null;
    }

    @Override
    public Rating getUserRatingGiven(long userId) {
        return null;
    }
}
