package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.AuthorBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookAuthorBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookModelBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BookModelJdbcDaoTest {

    private static final String ISBN = "9788497592208";
    private static final String TITLE = "Test Book Title";
    private static final String PUBLISHER = "Test Publisher";
    private static final String DESCRIPTION = "Test Description";
    private static final Genre GENRE = Genre.FICTION;
    private static final int EDITION = 1;
    private static final Short PUBLICATION_YEAR = 2022;
    private static final boolean IS_HARDCOVER = true;
    private static final boolean IS_POCKET_EDITION = false;
    private static final int PAGES = 300;
    private static final int WEIGHT = 500;
    private static final Language LANGUAGE = Language.ENGLISH;
    
    private long imageId;

    @Autowired
    private BookModelDao bookModelDao;
    
    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
    	
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.update("INSERT INTO image (image) VALUES (?)", new byte[0]);
        imageId = jdbcTemplate.queryForObject("SELECT MAX(imageId) FROM image", Long.class);
    }

    @Test
    public void testCreateBookModel() throws SQLException {
    	
        jdbcTemplate.update("INSERT INTO image (image) VALUES (?)", new byte[0]);
        
        Long imageId = jdbcTemplate.queryForObject("SELECT MAX(imageId) FROM image", Long.class);
        
        long bookModelId = bookModelDao.createBookModel(ISBN, TITLE, PUBLISHER, DESCRIPTION, GENRE, EDITION, PUBLICATION_YEAR, IS_HARDCOVER, IS_POCKET_EDITION, BookDimension.MEDIUM, LANGUAGE, PAGES, WEIGHT, imageId);

        assertTrue(bookModelId > 0);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_model", "bookModelId = " + bookModelId));
    }
    
    @Test
    public void testGetGenreQtyByBookModel() {
    	
        long bookModelId = bookModelDao.createBookModel(ISBN, TITLE, PUBLISHER, DESCRIPTION, GENRE, EDITION, PUBLICATION_YEAR, IS_HARDCOVER, IS_POCKET_EDITION, BookDimension.MEDIUM, LANGUAGE, PAGES, WEIGHT, imageId);

        List<GenreWrapper> genres = bookModelDao.getGenreQtyByBookModel("");

        assertNotNull(genres);
        assertTrue(genres.size() > 0);
    }
}

