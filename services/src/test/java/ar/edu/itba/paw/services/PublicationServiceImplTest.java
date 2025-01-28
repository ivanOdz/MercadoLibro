package ar.edu.itba.paw.services;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.interfaces.exceptions.PublicationNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.PublicationState;

@RunWith(MockitoJUnitRunner.class)
public class PublicationServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @Mock
    private LocationService locationService;
    @Mock
    private PublicationService publicationService;
    @Mock
    private PublicationDao publicationDao;
    
    @InjectMocks
    private PublicationServiceImpl publicationDetailService;

    private static final long PUBLICATION_ID = 1;
    private static final PublicationState PUBLICATION_STATE = PublicationState.CURRENT;
    private static final long BOOK_ID = 100;
    private static final long OWNER_ID = 300;
    private static final long LOCATION_ID = 20;
    
    @Test
    public void testCreatePublication() {
        
    	Book book = spy(Book.class);
    	User user = spy(User.class);
        Location location = spy(Location.class);
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        Publication publication = spy(Publication.class);
        
        book.setBookId(BOOK_ID);
        user.setUserId(OWNER_ID);
        location.setLocationId(LOCATION_ID);
        publication.setPublicationState(PUBLICATION_STATE);
        publication.setPublicationId(PUBLICATION_ID);
        
        when(bookService.getBookById(BOOK_ID)).thenReturn(book);
        when(userService.findById(OWNER_ID)).thenReturn(user);
        when(locationService.findById(LOCATION_ID)).thenReturn(location);
        when(publicationDao.createPublication(book, user, locations, PUBLICATION_STATE)).thenReturn(publication);

        Publication publicationDetail = publicationDetailService.createPublication(BOOK_ID, user, LOCATION_ID);
        
        Assert.assertNotNull(publicationDetail);
    }
    
    @Test(expected = UserNotUnauthorizedException.class)
    public void testAddLocationUserNotUnauthorized() {

    	User user_1 = spy(User.class);
    	User user_2 = spy(User.class);
    	Location location = spy(Location.class);
    	Publication publication = spy(Publication.class);
    	
    	user_1.setUserId(OWNER_ID);
    	user_2.setUserId(OWNER_ID + 1);
    	location.setLocationId(LOCATION_ID);
    	publication.setUser(user_1);
    	publication.setPublicationId(PUBLICATION_ID);
    	
    	when(locationService.findById(LOCATION_ID)).thenReturn(location);
    	when(publicationDao.getPublicationByPublicationId(PUBLICATION_ID)).thenReturn(Optional.ofNullable(publication));
    	
    	publicationDetailService.addLocation(publication.getPublicationId(), location.getLocationId(), user_2);
    }

    @Test(expected = PublicationNotFoundException.class)
    public void testPublicationNotFoundException() {
    	
    	when(publicationDao.getPublicationByPublicationId(PUBLICATION_ID)).thenReturn(Optional.ofNullable(null));
    	
    	publicationDetailService.getPublicationByPublicationId(PUBLICATION_ID);
    }
}
