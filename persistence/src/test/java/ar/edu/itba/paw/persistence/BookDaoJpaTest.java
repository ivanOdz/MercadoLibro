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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.AuthorConstants;
import ar.edu.itba.paw.persistence.constants.BookConstants;
import ar.edu.itba.paw.persistence.constants.BookModelConstants;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class BookDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private BookDao bookDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	// void createBookRating(User user, BookModel bookModel, int rating);
	// void createBookImage(Book book, List<Image> images);
	// void setOwner(Book book, User user);
	
	@Test
	public void testGetBookById() {
		
		Optional<Book> maybeBook = bookDao.getBookById(BookConstants.ID_1);
		
		Assert.assertTrue(maybeBook.isPresent());
		Assert.assertEquals(BookConstants.BOOK_MODEL_ID_1, maybeBook.get().getBookModel().getBookModelId());
		Assert.assertEquals(BookConstants.OWNER_ID_1, maybeBook.get().getOwner().getUserId());
		Assert.assertEquals(BookConstants.AVAILABLE_1, maybeBook.get().isAvailable());
		Assert.assertEquals(BookState.valueOf(BookConstants.BOOK_STATE_1), maybeBook.get().getBookState());
		Assert.assertEquals((int)BookConstants.EXCHANGE_QTY_1, maybeBook.get().getExchangesQty());
	}
	
	@Test
	public void testCreateBook() {
		
		final Author author = em.merge(new Author(AuthorConstants.ID_8, AuthorConstants.NAME_8));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author);
		
		final BookModel bookModel = em.merge(new BookModel(	BookModelConstants.ID_10,
															BookModelConstants.ISBN_10,
															BookModelConstants.TITLE_10,
															BookModelConstants.EDITORIAL_10,
															BookModelConstants.DESCRIPTION_10,
															Genre.fromString(BookModelConstants.GENRE_10),
															(int)BookModelConstants.EDITION_10,
															(int)BookModelConstants.WEIGHT_10,
															(int)BookModelConstants.PAGES_10,
															Language.valueOf(BookModelConstants.LANGUAGE_10),
															BookDimension.valueOf(BookModelConstants.DIMENSION_10),
															(short)(int)BookModelConstants.PUBLICATION_YEAR_10,
															BookModelConstants.IS_POCKET_EDITION_10,
															BookModelConstants.IS_HARD_COVER_10,
															authors,
															null
														));
		
		final User user = em.merge(new User(UserConstants.ID_4, UserConstants.NAME_4, UserConstants.MAIL_4, UserConstants.PASSWORD_4, UserConstants.IMAGE_ID_4, UserConstants.VERIFICATION_CODE_4, UserConstants.IS_VERIFIED_4, UserConstants.LANGUAGE_4));
		
		final BookState bookState = BookState.GOOD;
		
		Book newBook = bookDao.createBook(bookModel, user, bookState);
		
		Assert.assertNotNull(newBook);
		Assert.assertTrue(BookConstants.BOOK_MODEL_ID_9 < newBook.getBookId());
		Assert.assertEquals(bookState, newBook.getBookState());
		Assert.assertEquals(bookModel, newBook.getBookModel());
		Assert.assertEquals(user, newBook.getOwner());
	}
}
