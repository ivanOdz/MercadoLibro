package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
//@Rollback
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Sql(scripts = "classpath:users.sql")
public class UserJdbcDaoTest {

    private static final int PREEXISTING_USER_ID = 500;
    private static final String USERNAME = "username";
    private static final String MAIL = "mail";
    private static final String PASSWORD = "password";
    private static final int VERIFICATIONCODE = 10;


    private UserJdbcDao userDao;

//    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() throws SQLException {
        // 1. Precondiciones

        // 2. Ejercito la class under test
        User user = userDao.createUser(USERNAME, MAIL, PASSWORD, VERIFICATIONCODE);

        // 3. Postcondiciones
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(MAIL, user.getMail());
        //assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users", String.format("username = '%s', USERNAME")));
    }

    @Test
    public void testFindById() throws SQLException {
        // 1. Precondiciones - existe un usuario

        // 2. Ejercitar
        Optional<User> maybeUser = userDao.findById(PREEXISTING_USER_ID);

        // 3. Postcondiciones
        assertTrue(maybeUser.isPresent());
        assertEquals(PREEXISTING_USER_ID, maybeUser.get().getId());
    }
}
