package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.UserRatingNotFound;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserReviewServiceImplTest {
	
	@Mock
	private UserReviewDao userReviewDao;
	
	@InjectMocks
	private UserReviewServiceImpl userReviewService;
	
    private static final long USER_ID_1 = 1L;
    
	@Test(expected = UserRatingNotFound.class)
	public void testGetUserRatingEarnedRatingNotFoundException() {
		
		userReviewService.getUserRatingEarned(USER_ID_1);
	}
}
