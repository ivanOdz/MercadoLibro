package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/*
@RunWith(MockitoJUnitRunner.class)
public class PublicationsServiceImplTest {
	
	@Mock
	private PublicationDao publicationDao;
	@InjectMocks
	private PublicationsServiceImpl publicationService;
	
	private List<Publication> mockPublications;
	
	@Before
	public void setUp() {
		
		mockPublications = new ArrayList<>();
		
		long publicationId = 1;
		long bookId = 100;
		long userId = 200;
		long locationId = 5;
		PublicationState publicationState = PublicationState.CURRENT;
		Timestamp publicationDatetime = new Timestamp(System.currentTimeMillis());
		
		mockPublications.add(new Publication(publicationId, bookId, userId, publicationState, publicationDatetime, locationId));
		mockPublications.add(new Publication(publicationId + 1, bookId + 1, userId + 1, publicationState, publicationDatetime, locationId + 1));
	}
	
	@Test
	public void testGetAllPublications() {
		
		when (publicationDao.getAllPublications()).thenReturn(mockPublications);
		
		List<Publication> publications = publicationService.getAllPublications();
		
		assertNotNull(publications);
		assertEquals(2, publications.size());
	}
	
	@Test
	public void testGetAllPublicationsFilteredBy() {
		
		String search = "Librox";
		int bookStateFilter = 1;
		int genreFilter = 2;
		long userId = 3;
		
		when (publicationDao.getAllPublicationsFilteredBy(search, bookStateFilter, genreFilter, userId)).thenReturn(mockPublications);
		
		List<Publication> filteredPublications = publicationService.getAllPublicationsFilteredBy(search, bookStateFilter, genreFilter, userId);
		
		assertNotNull(filteredPublications);
		assertEquals(2, filteredPublications.size());
	}
	
	@Test
	public void testCreatePublication() {
		
		long bookId = 1;
		long userId = 2;
		long locationId = 3;
		PublicationState publicationState = PublicationState.CURRENT;
		
		when (publicationDao.createPublication(bookId, userId, locationId, publicationState)).thenReturn(1L);
		
		long publicationId = publicationService.createPublication(bookId, userId, locationId, publicationState);
		
		assertEquals(1, publicationId);
	}
}
*/