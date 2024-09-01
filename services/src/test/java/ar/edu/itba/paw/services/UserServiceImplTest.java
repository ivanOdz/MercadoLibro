package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME = "username";
    private static final String MAIL = "mail";
//----------------------------------------------
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDao mock;
/* Este @Before es para cuando se corre con MockitoJUnitRunner => no se usa @Mock ni  @InjectMocks
    @Before
    public void setUp() {
        mock = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(mock);
    }
*/
    @Test
    public void testCreate() {
        // 1. Precondiciones
        Mockito.when(mock.createUser(eq(USERNAME), eq(MAIL))).thenReturn(new User(1, USERNAME, MAIL));

        // 2. Ejercito la class under test
        User user = userService.createUser(USERNAME, MAIL);

        // 3. Valido las precondiciones
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(MAIL, user.getMail());
    }

    @Test(expected = DuplicateKeyException.class)
    public void testCreateDuplicate() {
        // 1. Precondiciones
        Mockito.when(mock.createUser(eq(USERNAME), eq(MAIL))).thenThrow(DuplicateKeyException.class);

        // 2. Ejercito la class under test
        User user = userService.createUser(USERNAME, MAIL);

        // 3. Valido las precondiciones
        fail();
    }
}
