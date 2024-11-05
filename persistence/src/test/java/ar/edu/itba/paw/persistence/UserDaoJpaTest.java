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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
	public void testFindById() throws SQLException {
		
		Optional<User> maybeUser = userDao.findById(UserConstants.ID_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		
		Assert.assertEquals(UserConstants.ID_1, maybeUser.get().getUserId());
		Assert.assertEquals(UserConstants.NAME_1, maybeUser.get().getUsername());
		Assert.assertEquals(UserConstants.MAIL_1, maybeUser.get().getMail());
		Assert.assertEquals(UserConstants.PASSWORD_1, maybeUser.get().getPassword());
		Assert.assertEquals(UserConstants.LANGUAGE_1, maybeUser.get().getLanguage());
		
		Assert.assertNotEquals(UserConstants.NON_EXISTING_ID, maybeUser.get().getLanguage());
	}
	    
}