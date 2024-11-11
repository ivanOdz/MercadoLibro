package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @Mock
    private EmailService emailService;
    @Mock
    private LocationService locationService;
    @Mock
    private PublicationService publicationService;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private ExchangeDao exchangeDao;

    @InjectMocks
    private ExchangeServiceImpl exchangeServiceImpl;

    private static final long EXCHANGE_ID_1 = 1L;
    private static final long USER_ID_1 = 1L;
    private static final long BOOK_ID_1 = 1L;
    private static final long USER_ID_2 = 2L;
    private static final long BOOK_ID_2 = 2L;
    private static final long PUBLICATION_ID_1 = 1L;
    private static final long PUBLICATION_ID_2 = 1L;
    private static final PublicationState PUBLICATION_STATE_1 = PublicationState.OFFERED;
    private static final PublicationState PUBLICATION_STATE_2 = PublicationState.CURRENT;
    
    private static final long LOCATION_ID = 1L;  
    private static final int ACCEPT_CODE = 123456789;

	@Test(expected = ExchangeBadRequestException.class)
	public void testExchangeBadRequestException() {
		
        Location location = spy(Location.class);
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        location.setLocationId(LOCATION_ID);
        
        User user_1 = spy(User.class);
        User user_2 = spy(User.class);
        Book book_1 = spy(Book.class);
        Book book_2 = spy(Book.class);
        Publication publication_1 = spy(Publication.class);
        Publication publication_2 = spy(Publication.class);
        Exchange exchange = spy(Exchange.class);
        
        user_1.setUserId(USER_ID_1);
        book_1.setBookId(BOOK_ID_1);
        book_1.setOwner(user_1);
        publication_1.setPublicationId(PUBLICATION_ID_1);
        publication_1.setPublicationState(PUBLICATION_STATE_1);
        publication_1.setBook(book_1);
        
        user_2.setUserId(USER_ID_2);
        book_2.setBookId(BOOK_ID_2);
        book_2.setOwner(user_2);
        publication_2.setPublicationId(PUBLICATION_ID_2);
        publication_2.setPublicationState(PUBLICATION_STATE_2);
        publication_2.setBook(book_2);
        
        exchange.setExchangeId(EXCHANGE_ID_1);
        exchange.setAcceptCode(ACCEPT_CODE);
        exchange.setOfferer(publication_1);
        exchange.setRequester(publication_2);
        
        exchangeServiceImpl.exchange((int)exchange.getAcceptCode(), true);
	}
	
	@Test(expected = ExchangeNotFoundException.class)
	public void testGetExchangeByAcceptCodeNotFoundException() {
		
		exchangeServiceImpl.getExchangeByAcceptCode(ACCEPT_CODE);
	}
	
	@Test(expected = ExchangeNotFoundException.class)
	public void testGetExchangeByIdNotFoundException() {
		
		exchangeServiceImpl.getExchangeById(EXCHANGE_ID_1);
	}
	
	@Test(expected = ExchangeBadRequestException.class)
	public void testcofirmRequesterBadRequestException() {
		
		exchangeServiceImpl.cofirmRequester(ACCEPT_CODE);
	}
	
	@Test(expected = ExchangeBadRequestException.class)
	public void testcofirmOffererBadRequestException() {
		
		exchangeServiceImpl.cofirmOfferer(ACCEPT_CODE);
	}
}
