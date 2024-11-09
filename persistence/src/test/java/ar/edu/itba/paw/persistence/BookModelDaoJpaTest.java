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
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.AuthorConstants;
import ar.edu.itba.paw.persistence.constants.BookModelConstants;
import ar.edu.itba.paw.persistence.constants.PublicationConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class BookModelDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private BookModelDao bookModelDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
//    List<Author> createAuthors(List<String> authors);
//    void createBookAuthors(List<Long> authorsIds, long bookModelId);
//    PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, String currentPage, String sortType);
//    List<GenreWrapper> getGenreQtyByBookModel(String search);
	
	@Test
	public void testGetBookModelByBookModelId() {
		
		final Author author_1 = em.merge(new Author(AuthorConstants.ID_1, AuthorConstants.NAME_1));
		final Author author_2 = em.merge(new Author(AuthorConstants.ID_2, AuthorConstants.NAME_2));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author_1);
		authors.add(author_2);
		
		Optional<BookModel> maybeBookModel = bookModelDao.getBookModelByBookModelId(BookModelConstants.ID_1);
		
		Assert.assertTrue(maybeBookModel.isPresent());
		Assert.assertEquals(BookModelConstants.ID_1, maybeBookModel.get().getBookModelId());
		Assert.assertEquals(BookModelConstants.ISBN_1, maybeBookModel.get().getIsbn());
		Assert.assertEquals(BookModelConstants.TITLE_1, maybeBookModel.get().getTitle());
		Assert.assertEquals(BookModelConstants.EDITORIAL_1, maybeBookModel.get().getEditorial());
		Assert.assertEquals(BookModelConstants.DESCRIPTION_1, maybeBookModel.get().getDescription());
		Assert.assertEquals(Genre.fromString("genre." + BookModelConstants.GENRE_1), maybeBookModel.get().getGenre());
		Assert.assertEquals((int)BookModelConstants.EDITION_1, maybeBookModel.get().getEdition());
		Assert.assertEquals((int)BookModelConstants.WEIGHT_1, maybeBookModel.get().getWeight());
		Assert.assertEquals((int)BookModelConstants.PAGES_1, maybeBookModel.get().getPages());
		Assert.assertEquals(Language.valueOf(BookModelConstants.LANGUAGE_1), maybeBookModel.get().getBookLanguage());
		Assert.assertEquals(BookDimension.valueOf(BookModelConstants.DIMENSION_1), maybeBookModel.get().getDimension());
		Assert.assertEquals((short)(int)BookModelConstants.PUBLICATION_YEAR_1, maybeBookModel.get().getPublicationYear());
		Assert.assertEquals(null, maybeBookModel.get().getImage());
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Author author : maybeBookModel.get().getAuthors()) {
			
			if (author.getAuthorName().equals(BookModelConstants.AUTHOR_1_1)) {
				found_1 = true;
			}
			else if (author.getAuthorName().equals(BookModelConstants.AUTHOR_1_2)) {
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
	public void testCreateBookModel() {
		
		final Author author_1 = em.merge(new Author(AuthorConstants.NON_EXISTENT_ID_1, BookModelConstants.NON_EXISTENT_AUTHOR_1));
		final Author author_2 = em.merge(new Author(AuthorConstants.NON_EXISTENT_ID_2, BookModelConstants.NON_EXISTENT_AUTHOR_2));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author_1);
		authors.add(author_2);
		
		final BookModel newBookModel = bookModelDao.createBookModel(	BookModelConstants.NON_EXISTENT_ISBN,
																		BookModelConstants.NON_EXISTENT_TITLE,
																		BookModelConstants.NON_EXISTENT_EDITORIAL,
																		BookModelConstants.NON_EXISTENT_DESCRIPTION,
																		Genre.fromString("genre." + BookModelConstants.NON_EXISTENT_GENRE),
																		(int)BookModelConstants.NON_EXISTENT_EDITION,
																		(short)(int)BookModelConstants.NON_EXISTENT_PUBLICATION_YEAR,
																		BookModelConstants.NON_EXISTENT_IS_HARD_COVER,
																		BookModelConstants.NON_EXISTENT_IS_POCKET_EDITION,
																		BookDimension.valueOf(BookModelConstants.NON_EXISTENT_DIMENSION),
																		Language.valueOf(BookModelConstants.NON_EXISTENT_LANGUAGE),
																		(int)BookModelConstants.NON_EXISTENT_PAGES,
																		(int)BookModelConstants.NON_EXISTENT_WEIGHT,
																		null,
																		authors
																	);
		
		Assert.assertNotNull(newBookModel);
		Assert.assertTrue(BookModelConstants.ID_10 < newBookModel.getBookModelId());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_ISBN, newBookModel.getIsbn());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_TITLE, newBookModel.getTitle());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_EDITORIAL, newBookModel.getEditorial());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_DESCRIPTION, newBookModel.getDescription());
		Assert.assertEquals(Genre.fromString("genre." + BookModelConstants.NON_EXISTENT_GENRE), newBookModel.getGenre());
		Assert.assertEquals((int)BookModelConstants.NON_EXISTENT_EDITION, newBookModel.getEdition());
		Assert.assertEquals((int)BookModelConstants.NON_EXISTENT_WEIGHT, newBookModel.getWeight());
		Assert.assertEquals((int)BookModelConstants.NON_EXISTENT_PAGES, newBookModel.getPages());
		Assert.assertEquals(Language.valueOf(BookModelConstants.NON_EXISTENT_LANGUAGE), newBookModel.getBookLanguage());
		Assert.assertEquals(BookDimension.valueOf(BookModelConstants.NON_EXISTENT_DIMENSION), newBookModel.getDimension());
		Assert.assertEquals((short)(int)BookModelConstants.NON_EXISTENT_PUBLICATION_YEAR, newBookModel.getPublicationYear());
		Assert.assertEquals(null, newBookModel.getImage());
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Author author : newBookModel.getAuthors()) {
			
			if (author.getAuthorName().equals(BookModelConstants.NON_EXISTENT_AUTHOR_1)) {
				found_1 = true;
			}
			else if (author.getAuthorName().equals(BookModelConstants.NON_EXISTENT_AUTHOR_2)) {
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
	
	
}
	