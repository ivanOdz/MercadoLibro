package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.beans.factory.annotation.Value;
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

    public ExchangeServiceImpl(final ExchangeDao exchangeDao, final BookService bs, final PublicationService ps, final EmailService emailService) {
    	
        this.exchangeDao = exchangeDao;
        this.bs = bs;
        this.ps = ps;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public void initializeExchange(long bookId, String location, long offererPubId) {
        long requesterPubId = ps.createPublication(bookId, bs.getBookById(bookId).get().getOwner().getUserId(), location, PublicationState.OFFERED);

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Optional<Exchange> ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);

        // mail variables setup
        Map<String, Object> variables = new HashMap<>();

        User offerer = ex.get().getOfferer().getBook().getOwner();
        User requester = ex.get().getRequester().getBook().getOwner();

        Book bookOffered = ex.get().getOfferer().getBook();
        Book bookRequested = ex.get().getRequester().getBook();

        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedPublication", bookRequested.getBookModel().getTitle());
        variables.put("offeredPublication", bookOffered.getBookModel().getTitle());
        variables.put("validationUrl", webappUrl + "/createexchange?accept_code=" + ex.get().getAcceptCode() + "&state=true");
        variables.put("rejectionUrl", webappUrl + "/createexchange?accept_code=" + ex.get().getAcceptCode() + "&state=false");
        variables.put("exchangeUrl", webappUrl + "/offers"); //TODO: verificar el funcionamiento de esto

        emailService.sendEmail(offerer.getMail(), variables, "exchangeRequest", "Requesting", offerer.getLanguage());
    }

    @Transactional
    @Override
    public String exchange(int acceptCode, boolean state) {
    	
        Optional<Exchange> ex = Optional.empty();
        
        if (state)
            ex = exchangeDao.acceptExchange(acceptCode);
        else {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ex = exchangeDao.rejectExchange(acceptCode);
            exchangeDao.setEndDate(acceptCode, timestamp);
        }

        if (ex.isEmpty()) {
            // TODO: EXCEPTIONS
        }

        // --- email variables
        Map<String, Object> variables = new HashMap<>();
        Book bookOffered = ex.get().getOfferer().getBook();
        Book bookRequested = ex.get().getRequester().getBook();
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
        // link para redirigir a la página de exchanges que corresponda para el call-to-action de marcar como confirmado

        emailService.sendExchangeEmail(requester.getMail(), variables, state, requester.getLanguage());

        ps.terminatePublication(ex.get().getOfferer());
        ps.terminatePublication(ex.get().getRequester());


        switch (ex.get().getExchangeState()) {
            case ExchangeState.ACCEPTED: {
                return "exchange/accepted";
            }
            case ExchangeState.REJECTED: {
                return "exchange/rejected";
            }
            default:
                return "exchange/invalid";
        }
    }

    @Transactional
    @Override
    public void cofirmOfferer(int acceptCode) {
    	
        Optional<Exchange> ex = exchangeDao.confirmOfferer(acceptCode);

        if (ex.get().isConfirmed()) {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.updateExchangeStatus(acceptCode, ExchangeState.TERMINATED.getValue());
            exchangeDao.setEndDate(acceptCode, timestamp);
            bs.exchangeOwnership(ex.get().getOfferer().getBook(), ex.get().getRequester().getBook());
        }
    }

    @Transactional
    @Override
    public void cofirmRequester(int acceptCode) {
    	
        Optional<Exchange> ex = exchangeDao.confirmRequester(acceptCode);

        if (ex.get().isConfirmed()) {
        	
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            exchangeDao.updateExchangeStatus(acceptCode, ExchangeState.TERMINATED.getValue());
            exchangeDao.setEndDate(acceptCode, timestamp);
            bs.exchangeOwnership(ex.get().getOfferer().getBook(), ex.get().getRequester().getBook());
        }
    }

    @Override
    public Optional<Exchange> getExchangeByAcceptCode(int acceptCode) {
    	
        return exchangeDao.findByAcceptCode(acceptCode);
    }
    
    @Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
    	
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
