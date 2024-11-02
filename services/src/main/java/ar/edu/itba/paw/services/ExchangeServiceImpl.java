package ar.edu.itba.paw.services;

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

    @Transactional
    @Override
    public void initializeExchange(long bookId, long locationId, long offererPubId) {
    	
        Long userId = bs.getBookById(bookId).getOwner().getUserId();
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

    @Transactional
    @Override
    public String exchange(int acceptCode, boolean state) {
    	
        Exchange ex;
        
        if (state) {
            ex = exchangeDao.acceptExchange(acceptCode);
            bs.setAvailable(ex.getOfferer().getBook(), false);
            bs.setAvailable(ex.getRequester().getBook(), false);
        } else {
            ex = exchangeDao.rejectExchange(acceptCode);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.setEndDate(acceptCode, timestamp);
        }


        // --- email variables
//        Map<String, Object> variables = new HashMap<>();
        Book bookOffered = ex.getOfferer().getBook();
        Book bookRequested = ex.getRequester().getBook();
        User requester = bookRequested.getOwner();
        User offerer = bookOffered.getOwner();

        emailService.sendExchangeEmail(requester, offerer, bookRequested, bookOffered, state);

        ps.terminatePublication(ex.getOfferer());
        ps.terminatePublication(ex.getRequester());

        return switch (ex.getExchangeState()) {
            case ExchangeState.ACCEPTED -> "exchange/accepted";
            case ExchangeState.REJECTED -> "exchange/rejected";
            default -> "exchange/invalid";
        };
    }

    @Transactional
    @Override
    public void cofirmOfferer(int acceptCode) {
        Exchange ex = exchangeDao.confirmOfferer(acceptCode);
        exchangeCompleted(acceptCode, ex);
    }

    @Transactional
    @Override
    public void cofirmRequester(int acceptCode) {
        Exchange ex = exchangeDao.confirmRequester(acceptCode);
        exchangeCompleted(acceptCode, ex);
    }

    private void exchangeCompleted(int acceptCode, Exchange ex) {
        if (ex.isConfirmed()) {
            exchangeDao.updateExchangeStatus(acceptCode, ExchangeState.TERMINATED);
            bs.exchangeOwnership(ex.getOfferer().getBook(), ex.getRequester().getBook());

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.setEndDate(acceptCode, timestamp);
        }
    }

    @Override
    public Exchange getExchangeByAcceptCode(int acceptCode) {
        return exchangeDao.findByAcceptCode(acceptCode);
    }
    
    @Override
    public Exchange getExchangeById(long exchangeId) {
    	return exchangeDao.getExchangeById(exchangeId);
    }

    // exchanges where user is the publication owner
    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, true);
    }

    // exchanges where user is the requester owner
    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, currentPage, false);
    }
}
