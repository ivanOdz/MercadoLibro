package ar.edu.itba.paw.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

//@Transactional
//@Rollback
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class UserReviewJdbcDaoTest {
//
//    @Autowired
//    private UserReviewDao userReviewDao;
//
//    @Autowired
//    private DataSource ds;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//    }
//
//    @Test
//    public void testGetReviewsGivenByNonExistentUser() {
//
//        long nonExistentUserId = 9999L;
//
//        PaginatedResponse<UserReview, BasicMetadata> reviews = userReviewDao.getReviewsGivenByUserId(nonExistentUserId, 1);
//        assertNotNull(reviews);
//        assertTrue(reviews.getData().isEmpty());
//    }
//
//    @Test
//    public void testGetReviewsEarnedByNonExistentUser() {
//
//        long nonExistentUserId = 9999L;
//
//        PaginatedResponse<UserReview, BasicMetadata> reviews = userReviewDao.getReviewsEarnedByUserId(nonExistentUserId, 1);
//        assertNotNull(reviews);
//        assertTrue(reviews.getData().isEmpty());
//    }
//
//    @Test
//    public void testGetUserReviewEarnedNonExistentExchange() {
//
//        long nonExistentExchangeId = 9999L;
//        long subjectId = 2;
//
//        UserReview review = userReviewDao.getUserReviewEarned(nonExistentExchangeId, subjectId);
//        assertNull(review);
//    }
//
//    @Test
//    public void testGetUserReviewGivenNonExistentExchange() {
//
//        long nonExistentExchangeId = 9999L;
//        long reviewerId = 1;
//
//        UserReview review = userReviewDao.getUserReviewGiven(nonExistentExchangeId, reviewerId);
//        assertNull(review);
//    }
//
//    @Test
//    public void testGetUserRatingEarnedByUserWithoutReviews() {
//
//        long userWithoutReviewsId = 3;
//
//        Rating rating = userReviewDao.getUserRatingEarned(userWithoutReviewsId);
//        assertNotNull(rating);
//        assertEquals(5.0, rating.getRating(), 0.1);	// Por defecto están en 5 estrellas :)
//        assertEquals(0, rating.getRatingCount());
//    }
//
//    @Test
//    public void testGetUserRatingGivenByUserWithoutReviews() {
//
//        long userWithoutReviewsId = 3;
//
//        Rating rating = userReviewDao.getUserRatingGiven(userWithoutReviewsId);
//        assertNotNull(rating);
//        assertEquals(5.0, rating.getRating(), 0.01);
//        assertEquals(0, rating.getRatingCount());
//    }
//}

