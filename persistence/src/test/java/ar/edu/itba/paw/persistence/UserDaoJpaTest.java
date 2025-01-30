package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.LocationConstants;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserDao userDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Test
	public void testFindById() {
		
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
		Assert.assertEquals(UserConstants.NAME_1, maybeUser.get().getUsername());
		Assert.assertEquals(UserConstants.MAIL_1, maybeUser.get().getMail());
		Assert.assertEquals(UserConstants.PASSWORD_1, maybeUser.get().getPassword());
		Assert.assertEquals(UserConstants.LANGUAGE_1, maybeUser.get().getLanguage());
		
		Assert.assertNotEquals(UserConstants.NON_EXISTING_ID, maybeUser.get().getLanguage());
	}
	
	@Test
	public void testFindByIdNonExistent() {
		
		Optional<User> maybeUser = userDao.findById(UserConstants.NON_EXISTING_ID);
		
		Assert.assertFalse(maybeUser.isPresent());
	}
	
	@Test
	public void testFindByMail() {
		
		Optional<User> maybeUser = userDao.findByMail(UserConstants.MAIL_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
	}
	
	@Test
	public void testFindByName() {
		
		Optional<User> maybeUser = userDao.findByUsername(UserConstants.NAME_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
	}
	
	@Test
	@Rollback
	public void testChangePassword() {

		final User user = em.merge(new User(UserConstants.ID_1, UserConstants.NAME_1, UserConstants.MAIL_1, UserConstants.PASSWORD_1, UserConstants.IMAGE_ID_1, UserConstants.VERIFICATION_CODE_1, UserConstants.IS_VERIFIED_1, UserConstants.LANGUAGE_1));
		final String newPassword = "newPass";
		
		userDao.changePassword(user, newPassword);
		em.flush();
		
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "userId = " + UserConstants.ID_1 + " AND password = '" + UserConstants.PASSWORD_1 + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "userId = " + UserConstants.ID_1 + " AND password = '" + newPassword + "'"));
	}
	
	@Test
	@Rollback
	public void testUpdateUsername() {
		
		final User user = em.merge(new User(UserConstants.ID_1, UserConstants.NAME_1, UserConstants.MAIL_1, UserConstants.PASSWORD_1, UserConstants.IMAGE_ID_1, UserConstants.VERIFICATION_CODE_1, UserConstants.IS_VERIFIED_1, UserConstants.LANGUAGE_1));
		final String newUsername = "Mariano";
		
		userDao.updateUsername(user, newUsername);
		em.flush();
		
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "userId = " + UserConstants.ID_1 + " AND userName = '" + UserConstants.NAME_1 + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "userId = " + UserConstants.ID_1 + " AND userName = '" + newUsername + "'"));
	}

	@Test
	@Rollback
	public void testAddUserLocation() {
		
		final User user = em.merge(new User(UserConstants.ID_1, UserConstants.NAME_1, UserConstants.MAIL_1, UserConstants.PASSWORD_1, UserConstants.IMAGE_ID_1, UserConstants.VERIFICATION_CODE_1, UserConstants.IS_VERIFIED_1, UserConstants.LANGUAGE_1));
		final Location newLocation = em.merge(new Location(LocationConstants.ID_3, LocationConstants.STRING_3));
		int locationsSize = 0;
		
		locationsSize = user.getUserLocations().size();
		userDao.addUserLocation(user, newLocation);
		em.flush();
		
		Assert.assertEquals(locationsSize + 1, user.getUserLocations().size());
		Assert.assertTrue(user.getUserLocations().contains(newLocation));
	}
}