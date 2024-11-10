package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.BookConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
public class UserReviewDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserReviewDao userReviewDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	private final Long exchangeId = 1L;
	private final Long offererPubId = 2L;
	private final Long requesterPubId = 4L;
	private final Long acceptCode = 9999999L;
	private final Timestamp exchangeStartDate = Timestamp.valueOf("2024-09-15 00:00:00");
	private Timestamp exchangeEndDate = Timestamp.valueOf("2024-09-16 12:30:00");;
	private String exchangeState = "ACCEPTED";
	private Boolean offererReceivedBook = true;
	private Boolean requesterReceivedBook = true;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.update(" 	INSERT INTO exchange (exchangeId, offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)\r\n"
							+ " VALUES (" + exchangeId + ", " + offererPubId + ", " + requesterPubId + ", '" + exchangeState + "', " + acceptCode + ", " + offererReceivedBook + ", " + requesterReceivedBook + ", '" + exchangeStartDate + "', '" + exchangeEndDate + "')");
		jdbcTemplate.update(" 	UPDATE book SET ownerId = " + BookConstants.OWNER_ID_1 + "WHERE bookId = " + BookConstants.ID_4);
		jdbcTemplate.update(" 	UPDATE book SET ownerId = " + BookConstants.OWNER_ID_2 + "WHERE bookId = " + BookConstants.ID_1);
	}
	
//	PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId, int currentPage);
//	PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId, int currentPage);
//	Optional<UserReview> getUserReviewEarned(long exchangeId, long userId);
//	Optional<UserReview> getUserReviewGiven(long exchangeId, long userId);
//	Optional<Rating> getUserRatingEarned(long userId);
//	Optional<Rating> getUserRatingGiven(long userId);
	
//	UserReview createOrUpdateUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating);
	@Test
	@Rollback
	public void testCreateOrUpdateUserReview() {
		
		final String description = "OK!";
		final int rating = 3;
		
		UserReview newReview = userReviewDao.createOrUpdateUserReview(exchangeId, (long)offererPubId, (long)requesterPubId, description, rating);
		
		assertNotNull(newReview);
		
		
	}
}