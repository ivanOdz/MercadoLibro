package ar.edu.itba.paw.persistence;

import java.util.Optional;
import java.util.Set;

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

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.LocationConstants;
import ar.edu.itba.paw.persistence.constants.PublicationConstants;

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
	public void testFindById() {
		
		Optional<Location> maybeLocation = locationDao.findById(LocationConstants.ID_1);
		
		Assert.assertTrue(maybeLocation.isPresent());
		Assert.assertEquals(LocationConstants.STRING_1, maybeLocation.get().getLocationString());
	}
	
	@Test
	public void testGetLocationByPublicationId() {
		
		Set<Location> locations = locationDao.getLocationByPublicationId(PublicationConstants.ID_1);
		Boolean found_1 = false;
		Boolean found_2 = false;
		Boolean found_3 = false;
		
		for (Location location : locations) {
			
			if (location.getLocationId().equals(PublicationConstants.LOCATION_ID_1_1)) {
				found_1 = true;
			}
			else if (location.getLocationId().equals(PublicationConstants.LOCATION_ID_1_2)) {
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
	public void testNewLocation() {

		final String newLocationString = "newLocation";
		
		Location newLocation = locationDao.newLocation(newLocationString);
		em.flush();
		
		Assert.assertEquals(newLocationString, newLocation.getLocationString());
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "location", "locationId = " + newLocation.getLocationId() + " AND locationString = '" + newLocationString + "'"));
	}
}