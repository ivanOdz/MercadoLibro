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

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.LocationConstants;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class LocationDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private LocationDao locationDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Test
	public void testFindById() throws SQLException {
		
		Optional<Location> maybeLocation = locationDao.findById(LocationConstants.ID_1);
		
		Assert.assertTrue(maybeLocation.isPresent());
		
		Assert.assertEquals(LocationConstants.STRING_1, maybeLocation.get().getLocationString());
	}
	
//	@Test
//	@Rollback
//	public void testNewLocation() throws SQLException {
//
//		final String newLocationString = "newLocation";
//		
//		Location newLocation = locationDao.newLocation(newLocationString);
//		
//		Assert.assertEquals(newLocationString, newLocation.getLocationString());
//	}
}