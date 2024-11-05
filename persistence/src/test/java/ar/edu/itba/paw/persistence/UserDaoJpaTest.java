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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoJpaTest {
	
	private static final long USER_ID_1 = 1;
	private static final long USER_VERIFICATION_CODE_1 = 123456789;
	private static final boolean USER_IS_VERIFIED_1 = true;
	private static final String USER_NAME_1 = "PROUSER1";
	private static final String USER_MAIL_1 = "PROUSERMAIL1";
	private static final String USER_PASSWORD_1 = "PROUSERPASSWORD1";
	private static final String USER_LANGUAGE_1 = "es";
	
	private static final long USER_ID_2 = 1;
	private static final long USER_VERIFICATION_CODE_2 = 987654321;
	private static final boolean USER_IS_VERIFIED_2 = true;
	private static final String USER_NAME_2 = "PROUSER2";
	private static final String USER_MAIL_2 = "PROUSERMAIL2";
	private static final String USER_PASSWORD_2 = "PROUSERPASSWORD2";
	private static final String USER_LANGUAGE_2 = "en";
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserJpaDao userDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);

		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users"); // Así cada test empieza con una tabla de Users vacia
	}
	
	@Test
	public void testFindById() throws SQLException {
		
		jdbcTemplate.execute( "	INSERT INTO users (userId, username, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)"
							+ " VALUES (" + USER_ID_1 + ", '" + USER_NAME_1 + "', '" + USER_MAIL_1 + "', '" + USER_PASSWORD_1 + "', NULL, " + USER_VERIFICATION_CODE_1 + ", '" + USER_IS_VERIFIED_1 + "', '" + USER_LANGUAGE_1 + "', NULL)" );

		jdbcTemplate.execute( "	INSERT INTO users (userId, username, mail, password, imageId, verificationCode, isVerified, language, favoriteLocation)"
							+ " VALUES (" + USER_ID_2 + ", '" + USER_NAME_2 + "', '" + USER_MAIL_2 + "', '" + USER_PASSWORD_2 + "', NULL, " + USER_VERIFICATION_CODE_2 + ", '" + USER_IS_VERIFIED_2 + "', '" + USER_LANGUAGE_2 + "', NULL)" );
		
		Optional<User> maybeUser = userDao.findById(USER_ID_1);
		
		Assert.assertTrue(maybeUser.isPresent());
		
		Assert.assertEquals(USER_ID_1, maybeUser.get().getUserId());
		Assert.assertEquals(USER_NAME_1, maybeUser.get().getUsername());
		Assert.assertEquals(USER_MAIL_1, maybeUser.get().getMail());
		Assert.assertEquals(USER_PASSWORD_1, maybeUser.get().getPassword());
		Assert.assertEquals(USER_LANGUAGE_1, maybeUser.get().getUserLocations());
		
		Assert.assertNotEquals(USER_LANGUAGE_2, maybeUser.get().getLanguage());
	}
	    
}