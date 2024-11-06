package ar.edu.itba.paw.persistence;

import java.sql.SQLException;
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

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.BookConstants;
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
	
	// Book createBook(BookModel bookModel, User owner, BookState bookState);
	// void createBookRating(User user, BookModel bookModel, int rating);
	// void createBookImage(Book book, List<Image> images);
	// void setOwner(Book book, User user);
	// Book getBookById(long bookId);
	
	@Test
	public void testGetBookById() throws SQLException {
		
		Optional<Book> maybeBook = bookDao.getBookById(BookConstants.ID_1);	// Cambiar en BOOK... se deberia trabajar con un Optional<Book> para mantener todo consistente
		
		Assert.assertTrue(maybeBook.isPresent());
	}
	
}
