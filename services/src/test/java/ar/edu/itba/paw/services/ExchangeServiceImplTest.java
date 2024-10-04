package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/*
public class ExchangeServiceImplTest {

	private static final long BOOK_OWNER_ID = 25L;
	private static final long BOOK_MODEL_ID = 50L;
	private static final long BOOK_ID = 100L;
	private static final long EXCHANGE_ID = 1L;
	private static final long OFFERER_PUB_ID = 100L;
	private static final long REQUESTER_PUB_ID = 200L;
	private static final int ACCEPT_CODE = 12345;
	private static final Timestamp START_DATE = new Timestamp(System.currentTimeMillis() - 86400000L); // = -1 día
	private static final Timestamp END_DATE = new Timestamp(System.currentTimeMillis());
	private static final ExchangeState EXCHANGE_STATE = ExchangeState.PENDING;
	private static final Exchange TEST_EXCHANGE = new Exchange(EXCHANGE_ID, OFFERER_PUB_ID, REQUESTER_PUB_ID, EXCHANGE_STATE, ACCEPT_CODE, false, false, START_DATE, END_DATE);
	
	@Mock
	private ExchangeDao exchangeDao;
	@Mock
	private BookService bookService;
	@Mock
	private BookModelService bookModelService;
	@Mock
	private ImageService imageService;
	@Mock
	private PublicationService publicationService;
	@Mock
	private BookAuthorService bookAuthorService;
	@Mock
	private BookImageService bookImageService;
	@Mock
	private LocationService locationService;
	@Mock
	private UserService userService;
	@Mock
	private EmailService emailService;
	@Mock
	private UserReviewService userReviewService;
	@InjectMocks
	private ExchangeServiceImpl exchangeService;
	
	@Test
	public void testGetExchangeById() {
		
		when (exchangeDao.findById(EXCHANGE_ID)).thenReturn(Optional.of(TEST_EXCHANGE));
		
		Optional<Exchange> result = exchangeService.getExchangeById(EXCHANGE_ID);
		
		assertTrue(result.isPresent());
		assertEquals(TEST_EXCHANGE, result.get());
	}
	
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
	
	//@Test
	public void testInitializeExchange() {
		
		final String location = "Zona sur";
		final long locationId = 1L;
		final int exchangesQty = 2;
		final int rating = 5;
		final long publicationId = 200L;
		
		CompleteBook requesterComplete = mock(CompleteBook.class);
		Book book = new Book(BOOK_ID, BOOK_MODEL_ID, BOOK_OWNER_ID, BookState.GOOD, exchangesQty, rating);
		
		when (requesterComplete.getLocation()).thenReturn(location);
		when (requesterComplete.getSelectedBookId()).thenReturn(BOOK_ID);
		when (bookService.getBookById(BOOK_ID)).thenReturn(Optional.of(book));
		when (locationService.newLocation(location)).thenReturn(locationId);
		when (publicationService.createPublication(BOOK_ID, BOOK_OWNER_ID, locationId, PublicationState.OFFERED)).thenReturn(publicationId);
		
		exchangeService.initializeExchange(requesterComplete, OFFERER_PUB_ID);
		
		assertNotNull(requesterComplete.getBook());
		assertEquals(location, requesterComplete.getLocation());
		assertTrue(bookService.getBookById(BOOK_ID).isPresent());
		
		Book fetchedBook = bookService.getBookById(BOOK_ID).get();
		
		assertEquals(BOOK_ID, fetchedBook.getBookId());
		assertEquals(BOOK_MODEL_ID, fetchedBook.getBookModelId());
		assertEquals(BOOK_OWNER_ID, fetchedBook.getOwnerId());
		assertEquals(BookState.GOOD, fetchedBook.getBookState());
		assertEquals(exchangesQty, fetchedBook.getExchangesQty());
		assertEquals(rating, fetchedBook.getRating());
		
		long newLocationId = locationService.newLocation(location);
		assertEquals(locationId, newLocationId);
		
		long newPublicationId = publicationService.createPublication(BOOK_ID, BOOK_MODEL_ID, locationId, PublicationState.OFFERED);
		assertEquals(publicationId, newPublicationId);
	}
}*/
