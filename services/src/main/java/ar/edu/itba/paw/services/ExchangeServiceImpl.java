package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.ExchangeBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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

    @Autowired
    private MessageSource messageSource;

    @Value("#{environment.webappUrl}")
    private String webappUrl;

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

        Exchange ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);

        // mail variables setup
        User offerer = ex.getOfferer().getBook().getOwner();
        User requester = ex.getRequester().getBook().getOwner();
        Book bookOffered = ex.getOfferer().getBook();
        Book bookRequested = ex.getRequester().getBook();

        emailService.sendExchangeRequestEmail(requester, offerer, bookRequested, bookOffered, ex.getAcceptCode());
    }

    @Override
    @Transactional
    public String exchange(int acceptCode, boolean state) {
        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }
        Exchange exchange = ex.get();
        if (state) {
            exchangeDao.acceptExchange(exchange ,acceptCode);
            bs.setAvailable(exchange.getOfferer().getBook(), false);
            bs.setAvailable(exchange.getRequester().getBook(), false);
        } else {
            exchangeDao.rejectExchange(exchange,acceptCode);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
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

        return switch (exchange.getExchangeState()) {
            case ExchangeState.ACCEPTED -> "exchange/accepted";
            case ExchangeState.REJECTED -> "exchange/rejected";
            default -> "exchange/invalid";
        };
    }

    @Override
    @Transactional
    public void cofirmOfferer(int acceptCode) {
        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }
        Exchange exchange = ex.get();
        exchangeDao.confirmOfferer(getExchangeByAcceptCode(acceptCode), acceptCode);
        exchangeCompleted(acceptCode, exchange);
    }

    @Override
    @Transactional
    public void cofirmRequester(int acceptCode) {
        Optional<Exchange> ex = exchangeDao.findByAcceptCode(acceptCode);
        if (ex.isEmpty()) {
            throw new ExchangeBadRequestException("Invalid accept code, exchange not found");
        }
        Exchange exchange = ex.get();
        exchangeDao.confirmRequester(exchange, acceptCode);
        exchangeCompleted(acceptCode, exchange);
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
        Optional<Exchange> exchange = exchangeDao.findByAcceptCode(acceptCode);
        if (exchange.isEmpty()) {
            throw new ExchangeNotFoundException("Exchange not found");
        }
        return exchange.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Exchange getExchangeById(long exchangeId) {
    	Optional<Exchange> exchange = exchangeDao.getExchangeById(exchangeId);
        if (exchange.isEmpty()) {
            throw new ExchangeNotFoundException("Exchange not found");
        }
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
