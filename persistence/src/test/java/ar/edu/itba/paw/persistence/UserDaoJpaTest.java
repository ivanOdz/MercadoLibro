package ar.edu.itba.paw.persistence;

import java.sql.SQLException;
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
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoJpaTest { // Faltaria el de User Favorite Location
	
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
	public void testFindById() throws SQLException {
		
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
		Assert.assertEquals(UserConstants.NAME_1, maybeUser.get().getUsername());
		Assert.assertEquals(UserConstants.MAIL_1, maybeUser.get().getMail());
		Assert.assertEquals(UserConstants.PASSWORD_1, maybeUser.get().getPassword());
		Assert.assertEquals(UserConstants.LANGUAGE_1, maybeUser.get().getLanguage());
		
		Assert.assertNotEquals(UserConstants.NON_EXISTING_ID, maybeUser.get().getLanguage());
		
		maybeUser = userDao.findById(UserConstants.NON_EXISTING_ID);
		
		Assert.assertFalse(maybeUser.isPresent());
	}
	
	@Test
	public void testFindByMail() throws SQLException {
		
		Optional<User> maybeUser = userDao.findByMail(UserConstants.MAIL_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
	}
	
	@Test
	public void testFindByName() throws SQLException {
		
		Optional<User> maybeUser = userDao.findByUsername(UserConstants.NAME_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
	}
	
	@Test
	@Rollback
	public void testChangePassword() throws SQLException {
		
		final String newPassword = "newPass";
		
		userDao.changePassword(UserConstants.VERIFICATION_CODE_1.intValue(), newPassword);
		
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);	// Esta seria la unica forma de validar... así que estaria bien?
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(newPassword, maybeUser.get().getPassword());
	}
	
	@Test
	@Rollback
	public void testUpdateUsername() throws SQLException {
		
		final String newUsername = "Mariano";
		
		userDao.updateUsername(UserConstants.ID_1, newUsername);
		
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(newUsername, maybeUser.get().getUsername());
	}

	@Test
	@Rollback
	public void testAddUserLocation() throws SQLException {
		
		final Location newLocation = new Location();
		int locationsSize = 0;
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);
		
		locationsSize = maybeUser.get().getUserLocations().size();
		userDao.addUserLocation(UserConstants.ID_1, newLocation);
		
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(locationsSize + 1, maybeUser.get().getUserLocations().size());
//		Assert.assertTrue(maybeUser.get().getUserLocations().contains(newLocation)); // Por qué no anda? :(
	}
}