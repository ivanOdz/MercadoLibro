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

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
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
	@Rollback
	public void testCreateBook() {
		
		final Author author = em.merge(new Author(AuthorConstants.ID_8, AuthorConstants.NAME_8));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author);
		
		final BookModel bookModel = em.merge(new BookModel(	BookModelConstants.ID_10,
															BookModelConstants.ISBN_10,
															BookModelConstants.TITLE_10,
															BookModelConstants.EDITORIAL_10,
															BookModelConstants.DESCRIPTION_10,
															Genre.fromString("genre." + BookModelConstants.GENRE_10),
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
	
	@Test
	@Rollback
	public void testCreateBookRating() {
		
		final Author author = em.merge(new Author(AuthorConstants.ID_2, AuthorConstants.NAME_2));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author);
		
		final BookModel bookModel = em.merge(new BookModel(	BookModelConstants.ID_2,
															BookModelConstants.ISBN_2,
															BookModelConstants.TITLE_2,
															BookModelConstants.EDITORIAL_2,
															BookModelConstants.DESCRIPTION_2,
															Genre.fromString("genre." + BookModelConstants.GENRE_2),
															(int)BookModelConstants.EDITION_2,
															(int)BookModelConstants.WEIGHT_2,
															(int)BookModelConstants.PAGES_2,
															Language.valueOf(BookModelConstants.LANGUAGE_2),
															BookDimension.valueOf(BookModelConstants.DIMENSION_2),
															(short)(int)BookModelConstants.PUBLICATION_YEAR_2,
															BookModelConstants.IS_POCKET_EDITION_2,
															BookModelConstants.IS_HARD_COVER_2,
															authors,
															null
														));
		
		final User user = em.merge(new User(UserConstants.ID_1, UserConstants.NAME_1, UserConstants.MAIL_1, UserConstants.PASSWORD_1, UserConstants.IMAGE_ID_1, UserConstants.VERIFICATION_CODE_1, UserConstants.IS_VERIFIED_1, UserConstants.LANGUAGE_1));
		
		bookDao.createBookRating(user, bookModel, 4);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_rating", "userId = " + user.getUserId() + " AND bookModelId = '" + bookModel.getBookModelId() + "' AND rating = 4"));
	}

	@Test
	@Rollback
	public void testSetOwner() {
		
		final Author author = em.merge(new Author(AuthorConstants.ID_2, AuthorConstants.NAME_2));
		final List<Author> authors = new ArrayList<Author>();
		authors.add(author);
		
		final BookModel bookModel = em.merge(new BookModel(	BookModelConstants.ID_2,
															BookModelConstants.ISBN_2,
															BookModelConstants.TITLE_2,
															BookModelConstants.EDITORIAL_2,
															BookModelConstants.DESCRIPTION_2,
															Genre.fromString("genre" + BookModelConstants.GENRE_2),
															(int)BookModelConstants.EDITION_2,
															(int)BookModelConstants.WEIGHT_2,
															(int)BookModelConstants.PAGES_2,
															Language.valueOf(BookModelConstants.LANGUAGE_2),
															BookDimension.valueOf(BookModelConstants.DIMENSION_2),
															(short)(int)BookModelConstants.PUBLICATION_YEAR_2,
															BookModelConstants.IS_POCKET_EDITION_2,
															BookModelConstants.IS_HARD_COVER_2,
															authors,
															null
														));
		
		final User oldOwner = em.merge(new User(UserConstants.ID_1, UserConstants.NAME_1, UserConstants.MAIL_1, UserConstants.PASSWORD_1, UserConstants.IMAGE_ID_1, UserConstants.VERIFICATION_CODE_1, UserConstants.IS_VERIFIED_1, UserConstants.LANGUAGE_1));
		final User newOwner = em.merge(new User(UserConstants.ID_2, UserConstants.NAME_2, UserConstants.MAIL_2, UserConstants.PASSWORD_2, UserConstants.IMAGE_ID_2, UserConstants.VERIFICATION_CODE_2, UserConstants.IS_VERIFIED_2, UserConstants.LANGUAGE_2));
		final Book book = em.merge(new Book(BookConstants.ID_2, oldOwner, bookModel, BookState.fromString(BookConstants.BOOK_STATE_2), (int)BookConstants.EXCHANGE_QTY_2, BookConstants.AVAILABLE_2, new ArrayList<BookImage>()));
		
		bookDao.setOwner(book, newOwner);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", "ownerId = " + newOwner.getUserId() + " AND bookModelId = " + bookModel.getBookModelId()));
	}
}
