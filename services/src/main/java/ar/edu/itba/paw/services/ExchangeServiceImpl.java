package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.MessageNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.utils.UrnResolverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeDao exchangeDao;

    @Autowired
    private BookService bs;

    @Autowired
    private PublicationService ps;

    @Autowired
    private EmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeServiceImpl.class);


    @Override
    @Transactional
    public Exchange initializeExchange(URI book, URI location, URI offererPub) {

        Long offererPubId = UrnResolverUtil.getPublicationId(offererPub);

        Long bookId = UrnResolverUtil.getBookId(book);

        Long locationId = UrnResolverUtil.getLocationId(location);

        Optional<Publication> publication = ps.getPublicationByPublicationId(offererPubId);
        if(publication.isPresent() && publication.get().getPublicationState() != PublicationState.CURRENT) {
            LOGGER.warn("Publication with id {} is not in current state", offererPub);
            throw new ExchangeBadRequestException("Publication is not in current state");
        }

    	Optional<Book> b = bs.getBookById(bookId);
    	
    	if(b.isEmpty()) {
            LOGGER.warn("Book with id {} is not existent", bookId);
            throw new ExchangeBadRequestException("Book not existent");
    	}
    	
        Long userId = b.get().getOwner().getUserId();
        long requesterPubId = ps.createPublication(bookId, userId, locationId).getPublicationId();

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());
        Date now = new Date();

        Exchange ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, new Timestamp(now.getTime()));
        
        if (ex != null) {
        	
            LOGGER.info("Created exchange with ID: {}", ex.getExchangeId());
            // mail variables setup
            User offerer = ex.getOfferer().getBook().getOwner();
            User requester = ex.getRequester().getBook().getOwner();
            Book bookOffered = ex.getOfferer().getBook();
            Book bookRequested = ex.getRequester().getBook();

            emailService.sendExchangeRequestEmail(requester, offerer, bookRequested, bookOffered, ex.getAcceptCode());
        }
        else {
            LOGGER.warn("Could not initialize exchange for book id {}", book);
        }
        return ex;
    }

    private boolean exchange(int acceptCode, boolean state) {
    	
        LOGGER.info("Processing exchange for acceptCode: {}", acceptCode);

        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }

        Exchange exchange = ex.get();
        LOGGER.info("Found exchange with ID: {} for acceptCode: {}", exchange.getExchangeId(), acceptCode);

        if (state) {
            LOGGER.info("Accepting exchange with acceptCode: {}", acceptCode);
            exchangeDao.acceptExchange(exchange ,acceptCode);
        } else {
            LOGGER.info("Rejecting exchange with acceptCode: {}", acceptCode);
            exchangeDao.rejectExchange(exchange,acceptCode);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            LOGGER.info("Setting end date for exchange with acceptCode: {} at timestamp: {}", acceptCode, timestamp);
            exchangeDao.setEndDate(exchange,acceptCode, timestamp);
            bs.setAvailable(exchange.getRequester().getBook(), true);
        }

        // --- email variables
        Book bookOffered = exchange.getOfferer().getBook();
        Book bookRequested = exchange.getRequester().getBook();
        User requester = bookRequested.getOwner();
        User offerer = bookOffered.getOwner();

        emailService.sendExchangeEmail(requester, offerer, bookRequested, bookOffered, state);

        ps.terminatePublication(exchange.getOfferer());
        ps.terminatePublication(exchange.getRequester());

        LOGGER.info("Exchange of id: {} processed successfully", exchange.getExchangeId());

        return exchange.getExchangeState() == ExchangeState.ACCEPTED;
    }

    private void confirmOffer(long userId, int acceptCode) {
    	
        LOGGER.info("Processing confirmOfferer for acceptCode: {}", acceptCode);

        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }

        Exchange exchange = ex.get();
        LOGGER.info("Found exchange with ID: {} for acceptCode: {}", exchange.getExchangeId(), acceptCode);

        if(exchange.getOfferer().getBook().getOwner().getUserId() == userId) {
            exchangeDao.confirmOfferer(getExchangeByAcceptCode(acceptCode).get(), acceptCode);
            exchangeCompleted(acceptCode, exchange);
            LOGGER.info("Confirmed offerer for acceptCode: {}", acceptCode);
        }
    }

    private void confirmRequest(long userId, int acceptCode) {
    	
        LOGGER.info("Processing confirmRequester for acceptCode: {}", acceptCode);

        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }
        Exchange exchange = ex.get();
        LOGGER.info("Found exchange with ID: {} for acceptCode: {}", exchange.getExchangeId(), acceptCode);

        if(exchange.getRequester().getBook().getOwner().getUserId() == userId) {
            exchangeDao.confirmRequester(exchange, acceptCode);
            LOGGER.info("Confirmed requester for acceptCode: {}", acceptCode);

            exchangeCompleted(acceptCode, exchange);
            LOGGER.info("Exchange completed for acceptCode: {}", acceptCode);
        }
    }


    private void exchangeCompleted(int acceptCode, Exchange ex) {
        if (ex.isConfirmed()) {
            exchangeDao.updateExchangeStatus(ex,acceptCode, ExchangeState.TERMINATED);
            bs.exchangeOwnership(ex.getOfferer().getBook(), ex.getRequester().getBook());

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.setEndDate(ex,acceptCode, timestamp);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exchange> getExchangeByAcceptCode(int acceptCode) {
    	
        LOGGER.info("Searching for exchange with acceptCode: {}", acceptCode);

        return exchangeDao.findByAcceptCode(acceptCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exchange> getExchangeById(long exchangeId) {
    	
        LOGGER.info("Searching for exchange with exchangeId: {}", exchangeId);

        return exchangeDao.getExchangeById(exchangeId);
    }

    // exchanges where user is the publication owner
//    @Override
//    @Transactional(readOnly = true)
//    public PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState) {
//        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, true);
//    }
//
//    // exchanges where user is the requester owner
//    @Override
//    @Transactional(readOnly = true)
//    public PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState) {
//        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, false);
//    }


    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getExchanges(URI user, ExchangeState exchangeState, Boolean isOfferer, Boolean isRequester, int currentPage) {
        // extract userId from urn
        UrnResolverUtil ur = new UrnResolverUtil(user.getPath());
        Long userId = ur.nextPath().nextPath().getId();

        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, isOfferer, isRequester);
    }

    @Override
    @Transactional
    public void createMessage(long exchangeId, URI user, String message) {
        UrnResolverUtil ur = new UrnResolverUtil(user.getPath());
        exchangeDao.createMessage(getExchangeById(exchangeId).get(), ur.nextPath().nextPath().getId(), message, new Timestamp((new Date()).getTime()));
    }

    @Override
    @Transactional
    public List<Message> getMessages(long exchangeId) {
        Optional<Exchange> e = getExchangeById(exchangeId);
        if (e.isEmpty()) {
        	return new ArrayList<Message>();
        }
        return e.get().getChat();
    }

    @Override
    public Message getMessage(long messageId) {
        Optional<Message> m =  exchangeDao.getMessageById(messageId);
        if (m.isEmpty()) {
            LOGGER.warn("Message not found for messageId: {}", messageId);
            throw new MessageNotFoundException("Message not found");
        }
        return m.get();
    }

    @Transactional
    @Override
    public void updateExchange(Integer acceptCode, Boolean accepted, Boolean requester) {
        Optional<Exchange> e = getExchangeByAcceptCode(acceptCode);
        if (accepted != null) {
            exchange(acceptCode,accepted);
        } else if (requester != null && e.isPresent()) {
            if (requester) {
                confirmRequest(e.get().getRequester().getBook().getOwner().getUserId(), acceptCode);
            } else {
                confirmOffer(e.get().getOfferer().getBook().getOwner().getUserId(), acceptCode);
            }
        } else {
            LOGGER.warn("Invalid parameters for updateExchange");
            throw new ExchangeBadRequestException("Invalid parameters for updateExchange");
        }
    }
}
