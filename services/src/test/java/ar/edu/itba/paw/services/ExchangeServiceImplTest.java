package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.Rating;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

//
//public class ExchangeServiceImplTest {
//
//    @Mock
//    private ExchangeDao exchangeDao;
//    @Mock
//    private BookService bookService;
//    @Mock
//    private PublicationService publicationService;
//    @Mock
//    private EmailService emailService;
//    @Mock
//    private MessageSource messageSource;
//
//    @InjectMocks
//    private ExchangeServiceImpl exchangeService;
//
//    private static final long BOOK_ID = 1L;
//    private static final String LOCATION = "Buenos Aires, Argentina";
//    private static final long OFFERER_PUB_ID = 1L;
//    private static final long REQUESTER_PUB_ID = 1L;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//    
//	User user1 = new User(1L, "Usuario1", "usuario1@example.com", "encodedPassword", 1L, 1234, true, "es-AR");
//	User user2 = new User(2L, "Usuario2", "usuario2@example.com", "encodedPassword", 2L, 4321, true, "es-AR");
//	User user3 = new User(3L, "Usuario3", "usuario3@example.com", "encodedPassword", 3L, 0123, true, "es-AR");
//	User user4 = new User(4L, "Usuario4", "usuario4@example.com", "encodedPassword", 4L, 3210, true, "es-AR");
//	
//	Genre genre1 = Genre.FICTION;
//	Genre genre2 = Genre.NON_FICTION;
//	Language language = Language.ENGLISH;
//	Rating rating = new Rating(4.5, 8);
//	
//	BookModel bookModel1 = new BookModel(1, "9783161484100", "Título1", "Editorial1", "Descripción1", genre1, 1, 300, 350, language, 20, (short) 2021, false, true, "Autor1", null, rating);
//	BookModel bookModel2 = new BookModel(2, "9781617290548", "Título2", "Editorial2", "Descripción2", genre2, 2, 250, 250, language, 15, (short) 2020, true, false, "Autor2", null, rating);
//	BookModel bookModel3 = new BookModel(3, "9780123748570", "Título3", "Editorial3", "Descripción3", genre1, 1, 400, 400, language, 22, (short) 2019, false, true, "Autor3", null, rating);
//	BookModel bookModel4 = new BookModel(4, "9780070428539", "Título4", "Editorial4", "Descripción4", genre2, 3, 350, 300, language, 25, (short) 2022, true, true, "Autor4", null, rating);
//	
//	Book book1 = new Book(Long.valueOf(1), user1, bookModel1, BookState.NEW, 3, true, List.of(1, 2));
//	Book book2 = new Book(Long.valueOf(2), user2, bookModel2, BookState.LIKE_NEW, 5, true, List.of(3, 4));
//	Book book3 = new Book(Long.valueOf(3), user3, bookModel3, BookState.VERY_GOOD, 2, false, List.of(5, 6));
//	Book book4 = new Book(Long.valueOf(4), user4, bookModel4, BookState.GOOD, 4, true, List.of(7, 8));
//	
//	Location location1 = new Location(Long.valueOf(1), "Buenos Aires, Argentina");
//	Location location2 = new Location(Long.valueOf(2), "Córdoba, Argentina");
//	Location location3 = new Location(Long.valueOf(3), "Mendoza, Argentina");
//	Location location4 = new Location(Long.valueOf(4), "Rosario, Argentina");
//	
//    Timestamp reviewDate = new Timestamp(System.currentTimeMillis());
//    Publication offererPub = new Publication(1, book1, PublicationState.CURRENT, reviewDate, location1);
//    Publication requesterPub = new Publication(2, book2, PublicationState.CURRENT, reviewDate, location2);
//    
//    @Test
//    public void testInitializeExchange() {
//    	
//        Book mockBook = book1;
//        User mockOfferer = user1;
//        User mockRequester = user2;
//        Publication mockPublication = offererPub;
//        Exchange mockExchange = new Exchange(1L, offererPub, requesterPub, ExchangeState.ACCEPTED, 123456, true, true, reviewDate, reviewDate);
//        
//        System.out.println(mockExchange);
//        
//        when(bookService.getBookById(BOOK_ID)).thenReturn(book1);
//        when(publicationService.createPublication(eq(BOOK_ID), eq(mockOfferer.getUserId()), eq(LOCATION), eq(PublicationState.OFFERED)))
//            .thenReturn(OFFERER_PUB_ID);
//        
//        when(exchangeDao.createExchange(eq(OFFERER_PUB_ID), eq(REQUESTER_PUB_ID), anyInt(), any())).thenReturn(mockExchange);
//        when(messageSource.getMessage(eq("email.subject.request"), isNull(), any(Locale.class))).thenReturn("Subject");
//        
//        exchangeService.initializeExchange(BOOK_ID, LOCATION, OFFERER_PUB_ID);
//        
//        assertNotNull(mockBook);
//        assertEquals(OFFERER_PUB_ID, publicationService.createPublication(BOOK_ID, mockOfferer.getUserId(), LOCATION, PublicationState.OFFERED));
//        assertNotNull(mockExchange);
//        
//        Map<String, Object> emailVariables = new HashMap<>();
//        emailVariables.put("requesterEmail", mockRequester.getMail());
//        emailVariables.put("requesterName", mockRequester.getUsername());
//        emailVariables.put("requestedPublication", mockBook.getBookModel().getTitle());
//
//        //assertEquals("Test Subject", messageSource.getMessage("email.subject.request", null, Locale.forLanguageTag(mockOfferer.getLanguage())));
//        //assertEquals(mockOfferer.getMail(), emailService.sendEmail(anyString(), emailVariables, eq("exchangeRequest"), eq("Test Subject"), anyString()));
//    }
//}

