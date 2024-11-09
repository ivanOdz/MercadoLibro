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
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.AuthorConstants;
import ar.edu.itba.paw.persistence.constants.BookConstants;
import ar.edu.itba.paw.persistence.constants.BookModelConstants;
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
	
//	Publication createPublication(Book book, User user, List<Location> locations, PublicationState publicationState);
//    void terminatePublication(Publication publication);
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
		
		final User user = em.merge(new User(	PublicationConstants.USER_ID_1,
												UserConstants.NAME_1,
												UserConstants.MAIL_1,
												UserConstants.PASSWORD_1,
												UserConstants.IMAGE_ID_1,
												UserConstants.VERIFICATION_CODE_1,
												UserConstants.IS_VERIFIED_1,
												UserConstants.LANGUAGE_1
											));
		
		final Author author_1 = em.merge(new Author(AuthorConstants.ID_1, BookModelConstants.AUTHOR_1_1));
		final Author author_2 = em.merge(new Author(AuthorConstants.ID_2, BookModelConstants.AUTHOR_1_2));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author_1);
		authors.add(author_2);
		
		final BookModel bookModel = em.merge(new BookModel( BookModelConstants.ID_1,
															BookModelConstants.ISBN_1,
															BookModelConstants.TITLE_1,
															BookModelConstants.EDITORIAL_1,
															BookModelConstants.DESCRIPTION_1,
															Genre.fromString("genre." + BookModelConstants.GENRE_1),
															(int)BookModelConstants.EDITION_1,
															(int)BookModelConstants.WEIGHT_1,
															(int)BookModelConstants.PAGES_1,
															Language.valueOf(BookModelConstants.LANGUAGE_1),
															BookDimension.valueOf(BookModelConstants.DIMENSION_1),
															(short)(int)BookModelConstants.PUBLICATION_YEAR_1,
															BookModelConstants.IS_POCKET_EDITION_1,
															BookModelConstants.IS_HARD_COVER_1,
															authors,
															null
														));
		
		final Book book = em.merge(new Book(	PublicationConstants.BOOK_ID_1,
												user,
												bookModel,
												BookState.fromString(BookConstants.BOOK_STATE_1),
												(int)BookConstants.EXCHANGE_QTY_1,
												BookConstants.AVAILABLE_1,
												new ArrayList<BookImage>()
											));
		
		final Location location_1 = new Location(LocationConstants.ID_1, LocationConstants.STRING_1);
		final Location location_2 = new Location(LocationConstants.ID_2, LocationConstants.STRING_2);
		final List<Location> locations = new ArrayList<Location>();
		locations.add(location_1);
		locations.add(location_2);
		
		final Publication publication = new Publication(	PublicationConstants.ID_1,
															book,
															user,
															PublicationState.valueOf(PublicationConstants.STATE_1),
															PublicationConstants.DATE_TIME_1,
															locations
														);
		
		publicationDao.terminatePublication(publication);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publication", "publicationId = " + PublicationConstants.ID_1 + "AND publicationState LIKE 'TERMINATED'"));
	}
}
