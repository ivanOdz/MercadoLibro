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

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.AuthorConstants;
import ar.edu.itba.paw.persistence.constants.BookModelConstants;

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
		Assert.assertEquals(BookModelConstants.EDITION_1, maybeBookModel.get().getEdition());
		Assert.assertEquals(BookModelConstants.WEIGHT_1, maybeBookModel.get().getWeight());
		Assert.assertEquals(BookModelConstants.PAGES_1, maybeBookModel.get().getPages());
		Assert.assertEquals(Language.valueOf(BookModelConstants.LANGUAGE_1), maybeBookModel.get().getBookLanguage());
		Assert.assertEquals(BookDimension.valueOf(BookModelConstants.DIMENSION_1), maybeBookModel.get().getDimension());
		Assert.assertEquals(BookModelConstants.PUBLICATION_YEAR_1.shortValue(), maybeBookModel.get().getPublicationYear().shortValue());
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
																		(int)BookModelConstants.NON_EXISTENT_WEIGHT
																	);
		
		em.flush();
		
		Assert.assertNotNull(newBookModel);
		Assert.assertTrue(BookModelConstants.ID_10 < newBookModel.getBookModelId());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_ISBN, newBookModel.getIsbn());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_TITLE, newBookModel.getTitle());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_EDITORIAL, newBookModel.getEditorial());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_DESCRIPTION, newBookModel.getDescription());
		Assert.assertEquals(Genre.fromString("genre." + BookModelConstants.NON_EXISTENT_GENRE), newBookModel.getGenre());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_EDITION, newBookModel.getEdition());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_WEIGHT, newBookModel.getWeight());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_PAGES, newBookModel.getPages());
		Assert.assertEquals(Language.valueOf(BookModelConstants.NON_EXISTENT_LANGUAGE), newBookModel.getBookLanguage());
		Assert.assertEquals(BookDimension.valueOf(BookModelConstants.NON_EXISTENT_DIMENSION), newBookModel.getDimension());
		Assert.assertEquals(BookModelConstants.NON_EXISTENT_PUBLICATION_YEAR.shortValue(), newBookModel.getPublicationYear().shortValue());
		Assert.assertEquals(null, newBookModel.getImage());
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_model", "isbn = " + BookModelConstants.NON_EXISTENT_ISBN + " AND title = '" + BookModelConstants.NON_EXISTENT_TITLE + "'"));
	}

	@Test
	@Rollback
	public void testCreateAuthors() {
		
		BookModel bookModel = new BookModel();
		bookModel.setBookModelId(BookModelConstants.ID_4);
		
		bookModel = bookModelDao.addAuthor(bookModel, BookModelConstants.NON_EXISTENT_AUTHOR_1);
		bookModel = bookModelDao.addAuthor(bookModel, BookModelConstants.NON_EXISTENT_AUTHOR_2);
		
		em.flush();
		Assert.assertNotNull(bookModel);
		Assert.assertNotNull(bookModel.getAuthors());
		
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Author author : bookModel.getAuthors()) {
			
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
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "author", "authorName = '" + BookModelConstants.NON_EXISTENT_AUTHOR_1 + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "author", "authorName = '" + BookModelConstants.NON_EXISTENT_AUTHOR_2 + "'"));
	}

	@Test
	public void testGetPaginatedBookModels() {
		
		final String search = "";
		final int currentPage = 0;
		final String sortType = "BOOK_NAME_ASCENDING";
		final Genre genreFilter = null;
		
		final PaginatedResponse<BookModel, BookModelMetadata> response = bookModelDao.getPaginatedBookModels(search, genreFilter, currentPage, sortType);
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMetadata());
		Assert.assertEquals(search, response.getMetadata().getSearch());
		Assert.assertEquals(currentPage, response.getMetadata().getCurrentPage());
		Assert.assertEquals(genreFilter, response.getMetadata().getGenreFilter());
		Assert.assertNotNull(response.getData());
		Assert.assertTrue(response.getData().size() > 0);
		
		Integer foundTimes_1 = 0;
		Integer foundTimes_2 = 0;
		Integer foundTimes_3 = 0;
		
		for (BookModel bookModel : response.getData()) {	// To assert is not repeted
			
			if (bookModel.getBookModelId() == BookModelConstants.ID_1) {
				foundTimes_1++;
			}
			else if (bookModel.getBookModelId() == BookModelConstants.ID_5) {
				foundTimes_2++;
			}
			else if (bookModel.getBookModelId() == BookModelConstants.ID_10) {
				foundTimes_3++;
			}
		}
		
		Assert.assertTrue(foundTimes_1 <= 1);
		Assert.assertTrue(foundTimes_2 <= 1);
		Assert.assertTrue(foundTimes_3 <= 1);
	}
	
	@Test
	public void testGetPaginatedBookModelsFilteredByGenreCrime() {
		
		final String search = "";
		final int currentPage = 0;
		final String sortType = "BOOK_NAME_ASCENDING";
		final Genre genreFilter = Genre.CRIME;
		
		final PaginatedResponse<BookModel, BookModelMetadata> response = bookModelDao.getPaginatedBookModels(search, genreFilter, currentPage, sortType);
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMetadata());
		Assert.assertEquals(search, response.getMetadata().getSearch());
		Assert.assertEquals(currentPage, response.getMetadata().getCurrentPage());
		Assert.assertEquals(genreFilter, response.getMetadata().getGenreFilter());
		Assert.assertNotNull(response.getData());
		Assert.assertTrue(response.getData().isEmpty());
	}
	
	@Test
	public void testGetPaginatedBookModelsFilteredByBookName() {
		
		final String search = "La sombra del viento";
		final int currentPage = 0;
		final String sortType = "BOOK_NAME_ASCENDING";
		final Genre genreFilter = null;
		
		final PaginatedResponse<BookModel, BookModelMetadata> response = bookModelDao.getPaginatedBookModels(search, genreFilter, currentPage, sortType);
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMetadata());
		Assert.assertEquals(search, response.getMetadata().getSearch());
		Assert.assertEquals(currentPage, response.getMetadata().getCurrentPage());
		Assert.assertEquals(genreFilter, response.getMetadata().getGenreFilter());
		Assert.assertNotNull(response.getData());
		Assert.assertTrue(response.getData().size() > 0);
		
		Boolean foundBook = false;
		
		for (BookModel bookModel : response.getData()) {
			
			if (bookModel.getBookModelId() == BookModelConstants.ID_3 && bookModel.getTitle().equals(search)) {
				foundBook = true;
				break;
			}
		}
		
		Assert.assertTrue(foundBook);
	}
	
	@Test
	public void testGetGenreQtyByBookModel() {
		
		final String search = "";
		
		List<GenreWrapper> genreWrapperList = bookModelDao.getGenreQtyByBookModel(search);
		
		int genreFantasy = 0;
		int genreClassic = 0;
		int genreFiction = 0;
		int genreThriller = 0;
		int genreMystery = 0;
		int genreOther = 0;
		int genreNonExistent = 0;
		
		for (GenreWrapper genreWrapper : genreWrapperList) {
			
			if (genreWrapper.getGenre() == Genre.FANTASY) {
				genreFantasy = genreWrapper.getResultByGenre();
			}
			else if (genreWrapper.getGenre() == Genre.CLASSIC) {
				genreClassic = genreWrapper.getResultByGenre();
			}
			else if (genreWrapper.getGenre() == Genre.FICTION) {
				genreFiction = genreWrapper.getResultByGenre();
			}
			else if (genreWrapper.getGenre() == Genre.THRILLER) {
				genreThriller = genreWrapper.getResultByGenre();
			}
			else if (genreWrapper.getGenre() == Genre.MYSTERY) {
				genreMystery = genreWrapper.getResultByGenre();
			}
			else if (genreWrapper.getGenre() == Genre.OTHER) {
				genreOther = genreWrapper.getResultByGenre();
			}
			else {
				genreNonExistent++;
			}
		}
		
		Assert.assertEquals(1, genreFantasy);
		Assert.assertEquals(1, genreClassic);
		Assert.assertEquals(2, genreFiction);
		Assert.assertEquals(1, genreThriller);
		Assert.assertEquals(2, genreMystery);
		Assert.assertEquals(3, genreOther);
		Assert.assertEquals(0, genreNonExistent);
	}
}
