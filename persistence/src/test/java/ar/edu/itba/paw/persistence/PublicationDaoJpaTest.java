package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.BookConstants;
import ar.edu.itba.paw.persistence.constants.LocationConstants;
import ar.edu.itba.paw.persistence.constants.PublicationConstants;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PublicationDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private PublicationDao publicationDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Test
	public void testGetPublicationByPublicationId() {
		
		Optional<Publication> maybePublication = publicationDao.getPublicationByPublicationId(PublicationConstants.ID_1);
		
		Assert.assertTrue(maybePublication.isPresent());
		Assert.assertNotNull(maybePublication.get().getBook());
		Assert.assertNotNull(maybePublication.get().getUser());
		Assert.assertEquals(PublicationConstants.ID_1, maybePublication.get().getPublicationId());
		Assert.assertEquals((long)PublicationConstants.BOOK_ID_1, maybePublication.get().getBook().getBookId());
		Assert.assertEquals(PublicationConstants.USER_ID_1, maybePublication.get().getUser().getUserId());	
		Assert.assertEquals(PublicationState.valueOf(PublicationConstants.STATE_1), maybePublication.get().getPublicationState());
		Assert.assertEquals(PublicationConstants.DATE_TIME_1, maybePublication.get().getPublicationDatetime());
		
		List<Location> locations = maybePublication.get().getLocations();
		
		Assert.assertNotNull(locations);
		Assert.assertNotEquals(0, maybePublication.get().getLocations().size());
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Location location : locations) {
			
			if (location.getLocationId() == PublicationConstants.LOCATION_ID_1_1) {
				found_1 = true;
			}
			else if (location.getLocationId() == PublicationConstants.LOCATION_ID_1_2) {
				found_2 = true;
			}
			else {
				found_3 = true;
				break;
			}
		}
		
		Assert.assertFalse(found_3);
		Assert.assertTrue(found_1);
		Assert.assertTrue(found_2);
	}
	
	@Test
	@Rollback
	public void testTerminatePublication() {
		
		final Publication publication = em.find(Publication.class, PublicationConstants.ID_1);
		
		publicationDao.terminatePublication(publication);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publication", "publicationId = " + PublicationConstants.ID_1 + "AND publicationState LIKE 'TERMINATED'"));
	}
	
	@Test
	@Rollback
	public void testCreatePublication() {
		// Book 3 selected (this one is not publicated yet)
		final User user = em.find(User.class, UserConstants.ID_1);
		final Book book = em.find(Book.class, BookConstants.ID_1);
		
		final Location location_1 = new Location(LocationConstants.ID_2, LocationConstants.STRING_2);
		final Location location_2 = new Location(LocationConstants.ID_3, LocationConstants.STRING_3);
		final List<Location> locations = new ArrayList<Location>();
		locations.add(location_1);
		locations.add(location_2);
		
		final Publication newPublication = publicationDao.createPublication(book, user, locations, PublicationState.CURRENT);
		
		Assert.assertNotNull(newPublication);
		Assert.assertNotNull(newPublication.getUser());
		Assert.assertNotNull(newPublication.getBook());
		Assert.assertTrue(PublicationConstants.ID_6 < newPublication.getPublicationId());
		Assert.assertEquals(user.getUserId(), newPublication.getUser().getUserId());
		Assert.assertEquals(book.getBookId(), newPublication.getBook().getBookId());
		
		List<Location> newPublicationLocations = newPublication.getLocations();
		
		Assert.assertNotNull(newPublicationLocations);
		Assert.assertNotEquals(0, newPublicationLocations.size());
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Location location : locations) {
			
			if (location.getLocationId() == LocationConstants.ID_2) {
				found_1 = true;
			}
			else if (location.getLocationId() == LocationConstants.ID_3) {
				found_2 = true;
			}
			else {
				found_3 = true;
				break;
			}
		}
		
		Assert.assertFalse(found_3);
		Assert.assertTrue(found_1);
		Assert.assertTrue(found_2);
	}
	
	@Test
	public void testGetPublicationCountByUserId() {
		
		Assert.assertEquals(PublicationConstants.COUNT_USER_1, publicationDao.getPublicationCountByUserId(UserConstants.ID_1));
		Assert.assertEquals(PublicationConstants.COUNT_USER_2, publicationDao.getPublicationCountByUserId(UserConstants.ID_2));
		Assert.assertEquals(PublicationConstants.COUNT_USER_3, publicationDao.getPublicationCountByUserId(UserConstants.ID_3));
		Assert.assertEquals(PublicationConstants.COUNT_USER_4, publicationDao.getPublicationCountByUserId(UserConstants.ID_4));
	}
	
	@Test
	public void testGetBookStateQtyByPublication() {
		
		final Long userId = UserConstants.ID_1;
		final String search = "";
		final Genre genreFilter = null;
		
		List<BookStateWrapper> bookStateWrapperList = publicationDao.getBookStateQtyByPublication(userId, search, genreFilter != null, genreFilter);
		
		int countStateWorn = 0;
		int countStateAcceptable = 0;
		int countStateGood = 0;
		int countStateVeryGood = 0;
		int countStateLikeNew = 0;
		int countStateNew = 0;
		
		for (BookStateWrapper bookStateWrapper : bookStateWrapperList) {
			
			if (bookStateWrapper.getBookState() == BookState.WORN) {
				countStateWorn++;
			}
			else if (bookStateWrapper.getBookState() == BookState.ACCEPTABLE) {
				countStateAcceptable++;
			}
			else if (bookStateWrapper.getBookState() == BookState.GOOD) {
				countStateGood++;
			}
			else if (bookStateWrapper.getBookState() == BookState.VERY_GOOD) {
				countStateVeryGood++;
			}
			else if (bookStateWrapper.getBookState() == BookState.LIKE_NEW) {
				countStateLikeNew++;
			}
			else if (bookStateWrapper.getBookState() == BookState.NEW) {
				countStateNew++;
			}
		}
		
		Assert.assertEquals(PublicationConstants.COUNT_STATE_WORN_USER_1, countStateWorn);
		Assert.assertEquals(PublicationConstants.COUNT_STATE_ACCEPTABLE_USER_1, countStateAcceptable);
		Assert.assertEquals(PublicationConstants.COUNT_STATE_GOOD_USER_1, countStateGood);
		Assert.assertEquals(PublicationConstants.COUNT_STATE_VERY_GOOD_USER_1, countStateVeryGood);
		Assert.assertEquals(PublicationConstants.COUNT_STATE_LIKE_NEW_USER_1, countStateLikeNew);
		Assert.assertEquals(PublicationConstants.COUNT_STATE_NEW_USER_1, countStateNew);
	}

	@Test
	@Rollback
	public void testDeletePublication() {
		
		publicationDao.deletePublication(PublicationConstants.ID_1);
		em.flush();
		
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publication", "publicationId = " + PublicationConstants.ID_1));
	}
	
	@Test
	public void testGetPaginatedPublications() {
		
		final Long userId = null;
		final String search = PublicationConstants.BOOK_NAME_4;
		final BookState bookStateFilter = null;
		final Genre genreFilter = null;
		final String sortType = "sort.book.name.ascending";
		final int currentPage = 0;
		final User currentUser = em.find(User.class, UserConstants.ID_3);
		
		PaginatedResponse<Publication, ItemFilterMetadata> response = publicationDao.getPaginatedPublications(	userId,
																												search,
																												bookStateFilter != null,
																												bookStateFilter,
																												genreFilter != null,
																												genreFilter,
																												sortType,
																												currentPage,
																												currentUser
																												);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMetadata());
		Assert.assertEquals(search, response.getMetadata().getSearch());
		Assert.assertEquals(currentPage, response.getMetadata().getCurrentPage());
		Assert.assertEquals(genreFilter, response.getMetadata().getGenreFilter());
		Assert.assertNotNull(response.getData());
		Assert.assertTrue(response.getData().size() > 0);
		
		Boolean foundPublication = false;
		
		for (Publication publication : response.getData()) {
			
			if (publication.getPublicationId() == PublicationConstants.ID_4 && publication.getUser().getUserId() == PublicationConstants.USER_ID_4) {
				foundPublication = true;
				break;
			}
		}
		
		Assert.assertTrue(foundPublication);
	}
	
// PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, String currentPage);
}
