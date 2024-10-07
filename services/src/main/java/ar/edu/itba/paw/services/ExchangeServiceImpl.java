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
	
    private final ExchangeDao exchangeDao;
    private final BookService bs;
    private final PublicationService ps;
    private final EmailService emailService;

    @Value("#{environment.webappUrl}")
    private String webappUrl;

    private final MessageSource messageSource;

    public ExchangeServiceImpl(final ExchangeDao exchangeDao, final BookService bs, final PublicationService ps, final EmailService emailService, final MessageSource messageSource) {
    	
        this.exchangeDao = exchangeDao;
        this.bs = bs;
        this.ps = ps;
        this.emailService = emailService;
        this.messageSource = messageSource;
    }

    @Transactional
    @Override
    public void initializeExchange(long bookId, String location, long offererPubId) {
        long requesterPubId = ps.createPublication(bookId,  bs.getBookById(bookId).getOwner().getUserId(), location, PublicationState.OFFERED);

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        Exchange ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);

        System.out.println("CREATE EXCHANGE " + ex);
        
        System.out.println(requesterPubId);
        System.out.println(acceptCode);
        System.out.println(timestamp);
        
        // mail variables setup
        Map<String, Object> variables = new HashMap<>();
        User offerer = ex.getOfferer().getBook().getOwner();
        User requester = ex.getRequester().getBook().getOwner();
        Book bookOffered = ex.getOfferer().getBook();
        Book bookRequested = ex.getRequester().getBook();

        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedPublication", bookRequested.getBookModel().getTitle());
        variables.put("offeredPublication", bookOffered.getBookModel().getTitle());
        variables.put("validationUrl", webappUrl + "/createexchange?accept_code=" + ex.getAcceptCode() + "&state=true");
        variables.put("rejectionUrl", webappUrl + "/createexchange?accept_code=" + ex.getAcceptCode() + "&state=false");
        variables.put("exchangeUrl", webappUrl + "/offers"); // TODO: verificar el funcionamiento de esto

        emailService.sendEmail(offerer.getMail(), variables, "exchangeRequest", messageSource.getMessage("email.subject.request", null, Locale.forLanguageTag(offerer.getLanguage())), offerer.getLanguage());
    }

    @Transactional
    @Override
    public String exchange(int acceptCode, boolean state) {
    	
        Exchange ex;
        
        if (state)
            ex = exchangeDao.acceptExchange(acceptCode);
        else {
            ex = exchangeDao.rejectExchange(acceptCode);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.setEndDate(acceptCode, timestamp);
        }


        // --- email variables
        Map<String, Object> variables = new HashMap<>();
        Book bookOffered = ex.getOfferer().getBook();
        Book bookRequested = ex.getRequester().getBook();
        User requester = bookRequested.getOwner();
        User offerer = bookOffered.getOwner();

        variables.put("requestedBook", bookRequested.getBookModel().getTitle());
        variables.put("offeredBook", bookOffered.getBookModel().getTitle());
        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offerer.getMail());
        variables.put("exchangeUrl", webappUrl + "/requests");
        variables.put("publicationsUrl", webappUrl);

        emailService.sendExchangeEmail(requester.getMail(), variables, state, requester.getLanguage());

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
            exchangeDao.updateExchangeStatus(acceptCode, ExchangeState.TERMINATED.getValue());
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
