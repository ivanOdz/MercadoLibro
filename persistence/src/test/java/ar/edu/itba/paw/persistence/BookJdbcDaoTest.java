package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.SortType;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BookJdbcDaoTest {

    private static final long BOOK_MODEL_ID = 1L;
    private static final long BOOK_ID = 1L;
    private static final long USER_ID = 1L;
    private static final BookState BOOK_STATE = BookState.GOOD;
    private static final int RATING = 5;
    
    @Autowired
    private BookDao bookDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        
    }

    @Test
    public void testCreateBook() throws SQLException {
    	
        User owner = userDao.findById(USER_ID).orElseThrow(() -> new SQLException("Usuario no encontrado"));
        Number bookId = bookDao.createBook(BOOK_MODEL_ID, owner, BOOK_STATE, new ArrayList<>());
        assertNotNull(bookId);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", "bookModelId = " + BOOK_MODEL_ID + " AND ownerId = " + USER_ID));
    }

//    @Test
//    public void testCreateBookRating() throws SQLException {
//        User user = userDao.findById(USER_ID).orElseThrow(() -> new SQLException("User not found"));
//        bookDao.createBookRating(user, BOOK_MODEL_ID, RATING);
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_rating", "userId = " + USER_ID + " AND bookModelId = " + BOOK_MODEL_ID));
//    }

 	@Test
    public void testGetBookById() throws SQLException {
    	
        Book book = bookDao.getBookById(BOOK_ID);
        assertNotNull(book);
        assertEquals(BOOK_ID, book.getBookId());
    }

//    @Test
//    public void testSetOwner() throws SQLException {
//
//        long newUserId = 2L;
//        bookDao.setOwner(BOOK_ID, newUserId);
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", "bookId = " + BOOK_ID + " AND ownerId = " + newUserId));
//    }
    
    @Test
    public void testGetAllBooksByUser() throws SQLException {
    	
        List<Book> books = bookDao.getAllBooksByUser(USER_ID);
        assertNotNull(books);
        assertTrue(books.size() > 0);
    }

    @Test
    public void testGetBookStateQtyByBook() {
    	
        List<BookStateWrapper> bookStates = bookDao.getBookStateQtyByBook("", false, null, USER_ID);
        assertNotNull(bookStates);
        assertTrue(bookStates.size() > 0);
    }

    @Test
    public void testGetGenreQtyByBook() {
    	
    	List<GenreWrapper> genres = bookDao.getGenreQtyByBook("", false, null, USER_ID);
        assertNotNull(genres);
        assertTrue(genres.size() > 0);
    }

    @Test
    public void testGetPaginatedBooks() {
    	
        PaginatedResponse<Book, ItemFilterMetadata> paginatedBooks = bookDao.getPaginatedBooks("Libro", false, BOOK_STATE, false, null, 1, USER_ID, SortType.RATING_ASCENDING);
        assertNotNull(paginatedBooks);
        //assertTrue(paginatedBooks.getData().size() > 0);
    }
}
