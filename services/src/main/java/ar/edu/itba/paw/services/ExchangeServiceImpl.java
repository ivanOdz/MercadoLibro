package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void initializeExchange(long bookId, long locationId, long offererPubId) {
    	Book book = bs.getBookById(bookId);
        Long userId = book.getOwner().getUserId();
        long requesterPubId = ps.createPublication(bookId,  userId, locationId, PublicationState.OFFERED).getPublicationId();

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        Optional<Exchange> ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);
        if(ex.isPresent()) {
            LOGGER.info("Created exchange with ID: {}", ex.get().getExchangeId());

            // mail variables setup
            User offerer = ex.get().getOfferer().getBook().getOwner();
            User requester = ex.get().getRequester().getBook().getOwner();
            Book bookOffered = ex.get().getOfferer().getBook();
            Book bookRequested = ex.get().getRequester().getBook();

            emailService.sendExchangeRequestEmail(requester, offerer, bookRequested, bookOffered, ex.get().getAcceptCode());
        }
        else{
            LOGGER.warn("Could not initialize exchange for book id {}", bookId);
        }
    }

    @Override
    @Transactional
    public String exchange(int acceptCode, boolean state) {
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
        }

        // --- email variables
        Book bookOffered = exchange.getOfferer().getBook();
        Book bookRequested = exchange.getRequester().getBook();
        User requester = bookRequested.getOwner();
        User offerer = bookOffered.getOwner();

        emailService.sendExchangeEmail(requester, offerer, bookRequested, bookOffered, state);

        ps.terminatePublication(exchange.getOfferer());
        ps.terminatePublication(exchange.getRequester());

        String redirect = switch (exchange.getExchangeState()) {
            case ExchangeState.ACCEPTED -> "exchange/accepted";
            case ExchangeState.REJECTED -> "exchange/rejected";
            default -> "exchange/invalid";
        };
        LOGGER.info("Exchange of id: {} processed successfully", exchange.getExchangeId());
        return redirect;
    }

    @Override
    @Transactional
    public void cofirmOfferer(int acceptCode) {
        LOGGER.info("Processing confirmOfferer for acceptCode: {}", acceptCode);

        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }

        Exchange exchange = ex.get();
        LOGGER.info("Found exchange with ID: {} for acceptCode: {}", exchange.getExchangeId(), acceptCode);

        exchangeDao.confirmOfferer(getExchangeByAcceptCode(acceptCode), acceptCode);
        exchangeCompleted(acceptCode, exchange);
        LOGGER.info("Confirmed offerer for acceptCode: {}", acceptCode);
    }

    @Override
    @Transactional
    public void cofirmRequester(int acceptCode) {
        LOGGER.info("Processing confirmRequester for acceptCode: {}", acceptCode);

        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }
        Exchange exchange = ex.get();
        LOGGER.info("Found exchange with ID: {} for acceptCode: {}", exchange.getExchangeId(), acceptCode);

        exchangeDao.confirmRequester(exchange, acceptCode);
        LOGGER.info("Confirmed requester for acceptCode: {}", acceptCode);

        exchangeCompleted(acceptCode, exchange);
        LOGGER.info("Exchange completed for acceptCode: {}", acceptCode);
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
    public Exchange getExchangeByAcceptCode(int acceptCode) {
        LOGGER.info("Searching for exchange with acceptCode: {}", acceptCode);

        Optional<Exchange> exchange = exchangeDao.findByAcceptCode(acceptCode);
        if (exchange.isEmpty()) {
            LOGGER.warn("Exchange not found for acceptCode: {}", acceptCode);
            throw new ExchangeNotFoundException("Exchange not found");
        }
        LOGGER.info("Exchange found for acceptCode: {}", acceptCode);
        return exchange.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Exchange getExchangeById(long exchangeId) {
        LOGGER.info("Searching for exchange with exchangeId: {}", exchangeId);

        Optional<Exchange> exchange = exchangeDao.getExchangeById(exchangeId);
        if (exchange.isEmpty()) {
            LOGGER.warn("Exchange not found for exchangeId: {}", exchangeId);
            throw new ExchangeNotFoundException("Exchange not found");
        }
        LOGGER.info("Exchange found for exchangeId: {}", exchangeId);
        return exchange.get();
    }

    // exchanges where user is the publication owner
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, String currentPage, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, true);
    }

    // exchanges where user is the requester owner
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, String currentPage, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, false);
    }

    @Override
    @Transactional
    public void createMessage(long exchangeId, long userId, String message) {
        exchangeDao.createMessage(getExchangeById(exchangeId), userId, message, new Timestamp((new Date()).getTime()));
    }
}
