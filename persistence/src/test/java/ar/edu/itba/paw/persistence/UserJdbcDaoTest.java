package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;

//@Transactional
//@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = "classpath:users.sql")
public class UserJdbcDaoTest {
    
    private static final String USERNAME = "username";
    private static final String NEW_USERNAME = "newUsername";
    private static final String MAIL = "mail";
    private static final String PASSWORD = "password";
    private static final int VERIFICATIONCODE = 10;
    private static final int PREEXISTING_USER_ID = 1;
    private static final String LANGUAGE = "en";
    private static final String NEW_LANGUAGE = "es";
    
    @Autowired
    private DataSource ds;
    
    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;
    
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }
    
    @Test
    public void testCreate() throws SQLException {
        
        User user = userDao.createUser(USERNAME, MAIL, PASSWORD, LANGUAGE, VERIFICATIONCODE);
        
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(MAIL, user.getMail());

        int count = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "username = '" + USERNAME + "'");
        
        assertEquals(1, count);
    }
    
    @Test
    public void testFindById() throws SQLException {

        Optional<User> maybeUser = userDao.findById(PREEXISTING_USER_ID);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(PREEXISTING_USER_ID, maybeUser.get().getUserId());
    }
    
    @Test
    public void testSetUserLanguage() throws SQLException {
        
        userDao.setUserLanguage(PREEXISTING_USER_ID, NEW_LANGUAGE);
        Optional<User> maybeUser = userDao.findById(PREEXISTING_USER_ID);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(NEW_LANGUAGE, maybeUser.get().getLanguage());
    }
    
    @Test
    public void testUpdateUsername() throws SQLException {
        
        boolean updated = userDao.updateUsername(PREEXISTING_USER_ID, NEW_USERNAME);
        assertTrue(updated);
        
        Optional<User> maybeUser = userDao.findById(PREEXISTING_USER_ID);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(NEW_USERNAME, maybeUser.get().getUsername());
    }
    
    @Test
    public void testFindByMail() throws SQLException {
        
        Optional<User> maybeUser = userDao.find(MAIL);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(MAIL, maybeUser.get().getMail());
    }
    
    @Test
    public void testFindByUsername() throws SQLException {
        
        Optional<User> maybeUser = userDao.findByUsername(USERNAME);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(USERNAME, maybeUser.get().getUsername());
    }
}
