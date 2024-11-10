package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.constants.AuthorConstants;
import ar.edu.itba.paw.persistence.constants.BookConstants;
import ar.edu.itba.paw.persistence.constants.BookModelConstants;
import ar.edu.itba.paw.persistence.constants.LocationConstants;
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
	private final Long acceptCode = 99999999L;
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
//    void setEndDate(Exchange exchange, int acceptCode, Timestamp endDate) ;
//    void acceptExchange(Exchange exchange, int acceptCode);
//    void confirmOfferer(Exchange exchange, int acceptCode);
//    void confirmRequester(Exchange exchange, int acceptCode);
//    void updateExchangeStatus(Exchange exchange, int acceptCode, ExchangeState newStatus);
//    Optional<Exchange> findByAcceptCode(int acceptCode) throws ExchangeNotFoundException;
//    Optional<Exchange> getExchangeById(long exchangeId);
//    PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, String currentPage, boolean isOfferer);
//    void createMessage(Exchange exchange, long userId, String message, Timestamp time);
	
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
	
//  void rejectExchange(Exchange exchange, int acceptCode);
	@Test
	@Rollback
	public void testRejectExchange() {
		// (Long exchangeId, Publication offerer, Publication requester, ExchangeState state, int acceptCode, boolean offererReceivedBook, boolean requesterReceivedBook, Timestamp exchangeStartDate, Timestamp exchangeEndDate, List<Message> chat)
		
		final Exchange exchange = em.find(Exchange.class, exchangeId);
		
		exchangeDao.rejectExchange(exchange, 123789456);
		em.flush();
		
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "exchange", "exchangeId = " + exchange.getExchangeId() + " AND exchangeState = 'REJECTED'"));
	}
	
}