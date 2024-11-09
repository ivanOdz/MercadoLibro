import java.sql.Timestamp;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.PublicationConstants;

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
	
//	Publication createPublication(Book book, User user, List<Location> locations, PublicationState publicationState);
//    void terminatePublication(Publication publication);
//    Optional<Publication> getPublicationByPublicationId(long publicationId);
//    PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(Long userId,String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String sortType, String currentPage, User currentUser);
//    int getPublicationCountByUserId(long userId);
//    List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter);
//    List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter);
//    void deletePublication(long publicationId);
//    void likePublication(long publicationId, long userId);
//    PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, String currentPage);
	
	@Test
	public void testGetPublicationByPublicationId() {
		
		Optional<Publication> maybePublication = publicationDao.getPublicationByPublicationId(PublicationConstants.ID_1);
		// (Long publicationId, Book book, User user, PublicationState publicationState, Timestamp publicationDatetime, List<Location> locations)
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
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Location location : locations) {
			
			if (location.equals(PublicationConstants.LOCATION_ID_1_1)) {
				found_1 = true;
			}
			else if (location.equals(PublicationConstants.LOCATION_ID_1_2)) {
				found_2 = true;
			}
			else {
				found_3 = true;
				break;
			}
		}
		
//		Assert.assertFalse(found_3);
//		Assert.assertTrue(found_1);
//		Assert.assertTrue(found_2);
	}
}
