package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	private static final long USER_ID = 1;
	private static final String USER_NAME = "userName";
	private static final String MAIL = "user@mail.com";
	private static final String PASSWORD = "password";
	private static final Long IMAGE_ID = null;
	private static final int VERIFICATION_CODE = 1234;
	private static final boolean IS_VERIFIED = true;
	
	@Mock
	private UserDao userDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private EmailService emailService;
	@Mock
	private UserReviewService userReviewsService;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	public void testCreate() {
		// 1. Precondiciones
		String encodedPassword = "encodedPassword";
		User mockUser = new User(USER_ID, USER_NAME, MAIL, encodedPassword, IMAGE_ID, VERIFICATION_CODE, IS_VERIFIED);
		when (passwordEncoder.encode(PASSWORD)).thenReturn(encodedPassword);
		when (userDao.createUser(eq(USER_NAME), eq(MAIL), eq(encodedPassword), anyInt())).thenReturn(mockUser);

		// 2. Ejercita la clase bajo prueba
		User user = userService.createUser(USER_NAME, MAIL, PASSWORD);
		
		// 3. Valida las postcondiciones
		assertNotNull(user);
		assertEquals(USER_NAME, user.getUsername());
		assertEquals(MAIL, user.getMail());
		assertEquals(encodedPassword, user.getPassword());
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void testCreateDuplicate() {
		
		String encodedPassword = "encodedPassword";
	    when (passwordEncoder.encode(PASSWORD)).thenReturn(encodedPassword);
	    when (userDao.createUser(eq(USER_NAME), eq(MAIL), eq(encodedPassword), anyInt()))
	    	.thenThrow(new DuplicateKeyException("Usuario ya existe"));
	
	    userService.createUser(USER_NAME, MAIL, PASSWORD);
	}
}
