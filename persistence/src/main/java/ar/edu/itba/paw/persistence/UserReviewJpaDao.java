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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.PROFILE_PAGE_SIZE;

@Primary
@Repository
public class UserReviewJpaDao implements UserReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating) {
        final UserReview userReview = new UserReview(null, em.find(User.class, userId), em.find(User.class, userSubjectId), em.find(Exchange.class, exchangeId), description, new Timestamp(new Date().getTime()), rating);
        em.persist(userReview);
    }

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage) {
        if(currentPage < 0){
            currentPage = 0;
        }
        int offset = currentPage * PROFILE_PAGE_SIZE;

        TypedQuery<UserReview> query = em.createQuery("FROM UserReview ur WHERE ur.reviewer.userId = :userId ORDER BY ur.reviewDate DESC", UserReview.class);

        query.setParameter("userId", userId);
        query.setMaxResults(PROFILE_PAGE_SIZE);
        query.setFirstResult(offset);

        List<UserReview> data = query.getResultList();

        return new PaginatedResponse<>(data, new BasicMetadata(currentPage, data.size(), PROFILE_PAGE_SIZE));
    }

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage) {
        if(currentPage < 0){
            currentPage = 0;
        }

        StringBuilder stringQuery = new StringBuilder("SELECT ur.userreviewid FROM user_review ur WHERE ur.subjectId = (:subject) ORDER BY ur.reviewDate DESC");

        Query nativeQuery = em.createNativeQuery(stringQuery.toString());
        nativeQuery.setParameter("subject", userId);
        nativeQuery.setMaxResults(PROFILE_PAGE_SIZE);
        nativeQuery.setFirstResult(currentPage * PROFILE_PAGE_SIZE);

        List<Long> reviewIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        TypedQuery<UserReview> query = em.createQuery("FROM UserReview ur WHERE ur.userReviewId IN :reviewIds ORDER BY ur.reviewDate DESC", UserReview.class);
        query.setParameter("reviewIds", reviewIds);


        List<UserReview> reviews = new ArrayList<>();
        try{
            reviews = query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }

        return new PaginatedResponse<>(reviews, new BasicMetadata(currentPage, reviews.size(), PROFILE_PAGE_SIZE));
    }

    @Override
    public UserReview getUserReviewEarned(long exchangeId, long userId) {
        String queryStr = "SELECT ur FROM UserReview ur WHERE ur.exchange.exchangeId = :exchangeId AND ur.subject.userId = :userId";
        TypedQuery<UserReview> query = em.createQuery(queryStr, UserReview.class);
        query.setParameter("exchangeId", exchangeId);
        query.setParameter("userId", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public UserReview getUserReviewGiven(long exchangeId, long userId) {
        String stringQuery = "FROM UserReview ur WHERE ur.exchange.exchangeId = :exchangeId AND ur.reviewer.userId = :userId";

        TypedQuery<UserReview> query = em.createQuery(stringQuery, UserReview.class);
        query.setParameter("exchangeId", exchangeId);
        query.setParameter("userId", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private Rating getRatingFromUserId(String stringQuery, long userId) {
        Query query = em.createNativeQuery(stringQuery);
        query.setParameter("userId", userId);

        Object[] result = (Object[]) query.getSingleResult();

        BigDecimal averageRatingBigDecimal = (BigDecimal) result[0];
        double averageRating = averageRatingBigDecimal.doubleValue();

        BigInteger countRatingBigInteger = (BigInteger) result[1];
        int countRating = countRatingBigInteger.intValue();

        return new Rating(averageRating, countRating);
    }

    @Override
    public Rating getUserRatingEarned(long userId) {
        String stringQuery = "SELECT COALESCE(AVG(ur.userreviewRating), 5.0), COUNT(ur.userreviewRating) FROM user_review ur WHERE ur.subjectId = :userId";
        return getRatingFromUserId(stringQuery, userId);
    }

    @Override
    public Rating getUserRatingGiven(long userId) {
        String stringQuery = "SELECT COALESCE(AVG(ur.userreviewRating), 5.0) AS averageRating, COUNT(ur.userreviewRating) AS countRating FROM user_review ur WHERE ur.reviewerId = :userId";
        return getRatingFromUserId(stringQuery, userId);
    }
}
