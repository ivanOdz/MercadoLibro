package ar.edu.itba.paw.persistence;

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
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.BookConstants;

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
	// Book createBook(BookModel bookModel, User owner, BookState bookState);
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
	
//	@Test
//	public void testCreateBook() throws SQLException {
//		
//		// (BookModel bookModel, User owner, BookState bookState)
//		Optional<Book> maybeBook = bookDao.createBook(null, null, null);
//		
//	}
}
