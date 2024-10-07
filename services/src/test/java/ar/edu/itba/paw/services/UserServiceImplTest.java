package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

/*
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {							// Falla en el userService.createUser ya que devuelve null, passwordEncoder.encode(PASSWORD) dentro de la implementacion devuelve null
	
	private static final long USER_ID = 1;
	private static final String USER_NAME = "userName";
	private static final String MAIL = "user@mail.com";
	private static final String PASSWORD = "password";
	private static final Long IMAGE_ID = null;
	private static final int VERIFICATION_CODE = 1234;
	private static final boolean IS_VERIFIED = false;
	private static final String LANGUAGE = "es-AR";
	private static final String ENCODED_PASS = "encodedPassword";
	
	@Mock
	private PublicationDao pubDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
    private MessageSource messageSource;
	@Mock
	private EmailService emailService;
	@Mock
	private UserReviewService userReviewsService;
	@Mock
	private UserDao userDao;
	
	@InjectMocks
	private UserServiceImpl userService;
	
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
	@Test
	public void testCreate() {
		
		// 1. Precondiciones
		User mockUser = new User(USER_ID, USER_NAME, MAIL, PASSWORD, IMAGE_ID, VERIFICATION_CODE, IS_VERIFIED, LANGUAGE);
		
		when (passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASS);
		when (userDao.find(MAIL)).thenReturn(Optional.empty());
		when (messageSource.getMessage(eq("email.subject.verification"), eq(null), eq(Locale.forLanguageTag(mockUser.getLanguage())))).thenReturn("Mensaje_internacionalizado");
		when (userDao.createUser(eq(USER_NAME), eq(MAIL), eq(PASSWORD), eq(LANGUAGE), anyInt())).thenReturn(mockUser);
		
		// 2. Ejercita la clase bajo prueba
		User user = userService.createUser(USER_NAME, MAIL, PASSWORD, LANGUAGE);
		
		// 3. Valida las postcondiciones
		assertNotNull(user);
		assertEquals(USER_NAME, user.getUsername());
		assertEquals(MAIL, user.getMail());
		assertEquals(ENCODED_PASS, user.getPassword());
		assertEquals(LANGUAGE, user.getLanguage());
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void testCreateDuplicate() {
		
	    when (passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASS);
	    when (userDao.createUser(eq(USER_NAME), eq(MAIL), eq(ENCODED_PASS), eq(LANGUAGE), anyInt())).thenThrow(new DuplicateKeyException("Usuario ya existe"));
	
	    userService.createUser(USER_NAME, MAIL, PASSWORD, LANGUAGE);
	}
}*/
