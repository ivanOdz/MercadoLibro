package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	@Mock
	private PublicationDao pubDao;
	@Mock
    private MessageSource messageSource;
	@Mock
	private EmailService emailService;
	@Mock
	private UserReviewService userReviewsService;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserDao userDao;
	
	@InjectMocks
	private UserServiceImpl userService;
	
    @Before
    public void setUp() {
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
    }
    
	private static final long USER_ID = 1;
	private static final String USER_NAME = "userName";
	private static final String MAIL = "user@mail.com";
	private static final String PASSWORD = "password";
	private static final Long IMAGE_ID = null;
	private static final int VERIFICATION_CODE = 1234;
	private static final boolean IS_VERIFIED = false;
	private static final String LANGUAGE = "es-AR";
	private static final String ENCODED_PASS = "encodedPassword";
    
	@Test
	public void testCreateUser() {
		
		// 1. Precondiciones
		when (userDao.createUser(anyString(), anyString(), anyString(), anyString(), anyInt())).thenReturn(	new User(	USER_ID,
																														USER_NAME,
																														MAIL,
																														PASSWORD,
																														IMAGE_ID,
																														VERIFICATION_CODE,
																														IS_VERIFIED,
																														LANGUAGE
																													));
		// 2. Ejercita la clase bajo prueba
		User newUser = userService.createUser(USER_NAME, MAIL, PASSWORD, LANGUAGE);
		
		// 3. Valida las postcondiciones
		Assert.assertNotNull(newUser);
		Assert.assertEquals(USER_NAME, newUser.getUsername());
		Assert.assertEquals(MAIL, newUser.getMail());
		Assert.assertEquals(PASSWORD, newUser.getPassword());
		Assert.assertEquals(LANGUAGE, newUser.getLanguage());
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreateUserDuplicate() {
		
	    when (passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASS);
	    when (userDao.createUser(eq(USER_NAME), eq(MAIL), eq(ENCODED_PASS), eq(LANGUAGE), anyInt())).thenThrow(new DuplicateKeyException("Usuario ya existe"));
	
	    userService.createUser(USER_NAME, MAIL, PASSWORD, LANGUAGE);
	}
}
