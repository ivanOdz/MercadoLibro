package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.PublicationConstants;
import ar.edu.itba.paw.persistence.constants.UserConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
public class ExchangeDaoJpaTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private ExchangeDao exchangeDao;
	
	@PersistenceContext
	private EntityManager em;
    
	private JdbcTemplate jdbcTemplate;
	
	private final Long exchangeId = 1L;
	private final Long offererPubId = 2L;
	private final Long requesterPubId = 4L;
	private final Long acceptCode = 9999999L;
	private final Timestamp exchangeStartDate = Timestamp.valueOf("2024-09-15 00:00:00");
	private Timestamp exchangeEndDate = null;
	private String exchangeState = "PENDING";
	private Boolean offererReceivedBook = false;
	private Boolean requesterReceivedBook = false;
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.update("	INSERT INTO exchange (exchangeId, offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)\r\n"
							+ " VALUES (" + exchangeId + ", " + offererPubId + ", " + requesterPubId + ", '" + exchangeState + "', " + acceptCode + ", " + offererReceivedBook + ", " + requesterReceivedBook + ", '" + exchangeStartDate + "', NULL)");
	}

	@Test
	public void testGetExchangeById() {
		
		Optional<Exchange> maybeExchange = exchangeDao.getExchangeById(1);
		
		Assert.assertTrue(maybeExchange.isPresent());
		Assert.assertEquals(exchangeId, maybeExchange.get().getExchangeId());
		Assert.assertEquals(offererPubId, maybeExchange.get().getOfferer().getPublicationId());
		Assert.assertEquals(requesterPubId, maybeExchange.get().getRequester().getPublicationId());
		Assert.assertEquals((long)acceptCode, maybeExchange.get().getAcceptCode());
		Assert.assertEquals(ExchangeState.valueOf(exchangeState), maybeExchange.get().getExchangeState());
		Assert.assertEquals(offererReceivedBook, maybeExchange.get().isOffererReceivedBook());
		Assert.assertEquals(requesterReceivedBook, maybeExchange.get().isRequesterReceivedBook());
		Assert.assertEquals(exchangeStartDate, maybeExchange.get().getExchangeStartDate());
	}

	@Test
	public void testFindByAcceptCode() {
		
		Optional<Exchange> maybeExchange = exchangeDao.findByAcceptCode((int)(long)acceptCode);
		
		Assert.assertTrue(maybeExchange.isPresent());
		Assert.assertEquals(exchangeId, maybeExchange.get().getExchangeId());
		Assert.assertEquals(offererPubId, maybeExchange.get().getOfferer().getPublicationId());
		Assert.assertEquals(requesterPubId, maybeExchange.get().getRequester().getPublicationId());
		Assert.assertEquals((long)acceptCode, maybeExchange.get().getAcceptCode());
		Assert.assertEquals(ExchangeState.valueOf(exchangeState), maybeExchange.get().getExchangeState());
		Assert.assertEquals(offererReceivedBook, maybeExchange.get().isOffererReceivedBook());
		Assert.assertEquals(requesterReceivedBook, maybeExchange.get().isRequesterReceivedBook());
		Assert.assertEquals(exchangeStartDate, maybeExchange.get().getExchangeStartDate());
	}
	
	@Test
	@Rollback
	public void testCreateExchange() {
		
		final int newAcceptCode = 123654789;
		final Timestamp newTimeStamp = Timestamp.valueOf(LocalDateTime.now());
		
		Exchange newExchange = exchangeDao.createExchange(PublicationConstants.ID_5, PublicationConstants.ID_6, newAcceptCode, newTimeStamp);
		
		Assert.assertNotNull(newExchange);
		Assert.assertEquals(PublicationConstants.ID_5, newExchange.getOfferer().getPublicationId());
		Assert.assertEquals(PublicationConstants.ID_6, newExchange.getRequester().getPublicationId());
		Assert.assertEquals(newAcceptCode, newExchange.getAcceptCode());
		Assert.assertEquals(newTimeStamp, newExchange.getExchangeStartDate());
	}
	
	@Test
	@Rollback
	public void testRejectExchange() {
		
		final Exchange exchange = em.find(Exchange.class, exchangeId);
		
		exchangeDao.rejectExchange(exchange, (int)(long)acceptCode);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND exchangeState = 'REJECTED'"));
	}
	
	@Test
	@Rollback
	public void testAcceptExchange() {
		
		final Exchange exchange = em.find(Exchange.class, exchangeId);

		exchangeDao.acceptExchange(exchange, (int)(long)acceptCode);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND exchangeState = 'ACCEPTED'"));
	}
	
	@Test
	@Rollback
	public void testSetEndDate() {
		
		final Exchange exchange = em.find(Exchange.class, exchangeId);
		exchangeEndDate = Timestamp.valueOf(LocalDateTime.now());
		
		exchangeDao.setEndDate(exchange, (int)(long)acceptCode, exchangeEndDate);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND exchangeEndDate = '" + exchangeEndDate + "'"));
	}

	@Test
	@Rollback
	public void testConfirmOfferer() {

		final Exchange exchange = em.find(Exchange.class, exchangeId);

		exchangeDao.confirmOfferer(exchange, (int)(long)acceptCode);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND offererReceivedBook = TRUE"));
	
	}
	
	@Test
	@Rollback
	public void testConfirmRequester() {

		final Exchange exchange = em.find(Exchange.class, exchangeId);

		exchangeDao.confirmRequester(exchange, (int)(long)acceptCode);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND requesterReceivedBook = TRUE"));
	}
	
	@Test
	@Rollback
	public void testUpdateExchangeStatus() {

		final Exchange exchange = em.find(Exchange.class, exchangeId);
		exchangeState = "TERMINATED";
		
		exchangeDao.updateExchangeStatus(exchange, (int)(long)acceptCode, ExchangeState.valueOf(exchangeState));
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND exchangeState = '" + exchangeState + "'"));
	}
	
	@Test
	@Rollback
	public void testCreateMessage() {
		
		final Exchange exchange = em.find(Exchange.class, exchangeId);
		final long userId = exchange.getRequester().getUser().getUserId();
		final String message = "HI";
		final Timestamp time = Timestamp.valueOf(LocalDateTime.now());
		
		exchangeDao.createMessage(exchange, userId, message, time);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(	jdbcTemplate,
																	"message",
																	"exchangeId = "
																	+ exchange.getExchangeId()
																	+ " AND userId = "
																	+ userId
																	+ " AND message LIKE '"
																	+ message + "'"
																));
	}
	/*
	@Test
	public void testGetAllExchangesByUserId() {
		
		final int currentPage = 0;
		final boolean isOfferer = offererPubId != UserConstants.ID_1;
				
		PaginatedResponse<Exchange, BasicMetadata> response = exchangeDao.getAllExchangesByUserId(UserConstants.ID_1, ExchangeState.valueOf(exchangeState), currentPage, isOfferer);
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getMetadata());
		Assert.assertEquals(currentPage, response.getMetadata().getCurrentPage());
		Assert.assertNotNull(response.getData());
		Assert.assertTrue(response.getData().size() > 0);
		
		boolean found = false;
		
		for (Exchange exchange : response.getData()) {
			
			if (exchange.getExchangeId() == exchangeId) {
				found = true;
				break;
			}
		}
		
		Assert.assertTrue(found);
	}
	 */
}