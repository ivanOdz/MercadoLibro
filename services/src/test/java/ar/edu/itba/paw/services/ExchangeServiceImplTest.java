package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeBadRequestException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    private static final long PUBLICATION_ID_2 = 2L;
    private static final long OFFERER_ID_1 = 1L;
    private static final long REQUESTER_ID_1 = 2L;
    private static final PublicationState PUBLICATION_STATE_1 = PublicationState.OFFERED;
    private static final PublicationState PUBLICATION_STATE_2 = PublicationState.CURRENT;
    
    private static final long LOCATION_ID = 1L;  
    private static final int ACCEPT_CODE = 123456789;

	public void testInitializeExchange() {
		
        Location location = mock(Location.class);
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        location.setLocationId(LOCATION_ID);
        
        User user_1 = mock(User.class);
        User user_2 = mock(User.class);
        Book book_1 = mock(Book.class);
        Book book_2 = mock(Book.class);
        Publication publication_1 = mock(Publication.class);
        Publication publication_2 = mock(Publication.class);
        Exchange exchange = mock(Exchange.class);
        Exchange returnExchange = null;
        
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
        
        when(bookService.getBookById(BOOK_ID_1)).thenReturn(book_1);
        when(publicationService.createPublication(BOOK_ID_1, USER_ID_1, LOCATION_ID,true)).thenReturn(publication_1);
        when(exchangeDao.createExchange(BOOK_ID_1, REQUESTER_ID_1, ACCEPT_CODE, any())).thenReturn(exchange);
        
        returnExchange = exchangeServiceImpl.initializeExchange(BOOK_ID_1, LOCATION_ID, OFFERER_ID_1);
        
        Assert.assertNotNull(returnExchange);
        Assert.assertEquals(EXCHANGE_ID_1, returnExchange.getExchangeId().longValue());
        Assert.assertEquals(PUBLICATION_ID_1, returnExchange.getOfferer().getPublicationId().longValue());
        Assert.assertEquals(PUBLICATION_ID_2, returnExchange.getRequester().getPublicationId().longValue());
        Assert.assertEquals(ACCEPT_CODE, returnExchange.getAcceptCode());
	}

	@Test(expected = ExchangeBadRequestException.class)
	public void testGetExchangeByIdNotFoundException() {
		
		exchangeServiceImpl.getExchangeById(EXCHANGE_ID_1);
	}
	
	@Test(expected = ExchangeBadRequestException.class)
	public void testUpdateExchangeBadRequestException() {
		
		exchangeServiceImpl.updateExchange(ACCEPT_CODE, true, true);
	}
}
