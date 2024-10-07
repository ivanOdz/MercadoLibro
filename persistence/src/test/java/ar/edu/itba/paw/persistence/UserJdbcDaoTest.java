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

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = "classpath:users.sql")
public class UserJdbcDaoTest {
    
    private static final String USERNAME = "username";
    private static final String MAIL = "mail";
    private static final String PASSWORD = "password";
    private static final String LANGUAGE = "en";
    private static final int VERIFICATIONCODE = 0;
    
    @Autowired
    private UserJdbcDao userDao;
    
    @Autowired
    private DataSource ds;

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

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "username = '" + USERNAME + "'"));
    }

    @Test
    public void testFindById() throws SQLException {
    	
        User user = userDao.createUser(USERNAME, MAIL, PASSWORD, LANGUAGE, VERIFICATIONCODE);
        long userId = user.getUserId();
        
        Optional<User> maybeUser = userDao.findById(userId);
        
        assertTrue(maybeUser.isPresent());
        assertEquals(userId, maybeUser.get().getUserId());
    }

}
