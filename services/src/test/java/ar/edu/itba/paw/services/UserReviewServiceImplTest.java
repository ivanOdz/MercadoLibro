package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/*
@RunWith(MockitoJUnitRunner.class)
public class UserReviewServiceImplTest {

    @Mock
    private UserReviewDao userReviewDao;

    @InjectMocks
    private UserReviewServiceImpl userReviewService;

    @Test
    public void testGetReviewsByUserId() {

        long userId = 1;
        long exchangeId = 100;
        long reviewerId = 2;
        long subjectId = 200;
        String reviewDescription = "Buen libro :)";

        Timestamp reviewDate = new Timestamp(System.currentTimeMillis());
        int userReviewRating = 5;

        Publication offerer = new Publication(1, new Book(), PublicationState.CURRENT, reviewDate, new Location(subjectId, reviewDescription));
        Publication requester = new Publication(2, new Book(), PublicationState.CURRENT, reviewDate, new Location(subjectId, reviewDescription));

        Exchange exchange = new Exchange(exchangeId, offerer, requester, ExchangeState.ACCEPTED, 123456, true, true, reviewDate, reviewDate);

        List<UserReview> mockReviews = new ArrayList<>();

        UserReview mockReview = new UserReview(1, offerer, requester, exchange, reviewDescription, reviewDate, userReviewRating);
        mockReviews.add(mockReview);

        when(userReviewDao.getReviewsGivenByUserId(userId)).thenReturn(mockReviews);

        List<UserReview> reviews = userReviewService.getReviewsGivenByUserId(userId);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(reviewDescription, reviews.get(0).getReviewDescription());
        assertEquals(userReviewRating, reviews.get(0).getReviewRating());
    }

    @Test
    public void testGetUserReview() {

        long exchangeId = 1;
        long reviewerId = 2;
        long subjectId = 3;
        String reviewDescription = "Esta bien";
        Timestamp reviewDate = new Timestamp(System.currentTimeMillis());
        int userReviewRating = 4;

        Publication offerer = new Publication(1, new Book(), PublicationState.CURRENT, reviewDate, new Location(subjectId, reviewDescription));
        Publication requester = new Publication(2, new Book(), PublicationState.CURRENT, reviewDate, new Location(subjectId, reviewDescription));

        Exchange exchange = new Exchange(exchangeId, offerer, requester, ExchangeState.ACCEPTED, 123456, true, true, reviewDate, reviewDate);
        UserReview mockReview = new UserReview(1, offerer, requester, exchange, reviewDescription, reviewDate, userReviewRating);

        when(userReviewDao.getUserReview(exchangeId, reviewerId)).thenReturn(mockReview);

        UserReview review = userReviewService.getUserReview(exchangeId, reviewerId);

        assertNotNull(review);
        assertEquals(reviewDescription, review.getReviewDescription());
        assertEquals(userReviewRating, review.getReviewRating());
    }

    @Test
    public void testCreateUserReview() {

        long userReviewId = 1;
        long exchangeId = 2;
        long reviewerId = 3;
        long subjectId = 4;
        String reviewDescription = "Esta muy bien creo";
        Timestamp reviewDate = new Timestamp(System.currentTimeMillis());
        int userReviewRating = 5;

        Publication offerer = new Publication(1, new Book(), PublicationState.CURRENT, reviewDate, new Location());
        Publication requester = new Publication(2, new Book(), PublicationState.CURRENT, reviewDate, new Location());

        Exchange exchange = new Exchange(exchangeId, offerer, requester, ExchangeState.ACCEPTED, 123456, true, true, reviewDate, reviewDate);

        UserReview userReview = new UserReview(userReviewId, offerer, requester, exchange, reviewDescription, reviewDate, userReviewRating);

        when(userReviewDao.createUserReview(any(UserReview.class))).thenReturn(true);

        Boolean result = userReviewService.createUserReview(userReview);

        assertTrue(result);
    }
}
*/