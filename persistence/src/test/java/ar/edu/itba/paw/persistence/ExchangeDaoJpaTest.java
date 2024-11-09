package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
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

import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.persistence.config.TestConfig;

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
	
	@Before
	public void setup() {
		
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.update("	INSERT INTO exchange (exchangeId, offererPubId, requesterPubId, exchangeState, acceptCode, offererReceivedBook, requesterReceivedBook, exchangeStartDate, exchangeEndDate)\r\n"
							+ " VALUES (	1," 					// exchangeId
										+ " 2," 					// offererPubId
										+ " 4," 					// requesterPubId
										+ "'ACCEPTED'," 			// exchangeState
										+ "	123456789,"				// acceptCode
										+ " TRUE,"					// offererReceivedBook
										+ " TRUE,"					// requesterReceivedBook
										+ " '2024-09-15 00:00:00',"	// exchangeStartDate
										+ " '2024-09-15 01:00:00'"	// exchangeEndDate
										+ ");");
	}
	
//    Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate);
//    void rejectExchange(Exchange exchange, int acceptCode);
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
	}
	
}