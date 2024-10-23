package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

public class ExchangeJpaDao implements ExchangeDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate) {
        Publication offerer = em.find(Publication.class, offererPubId);
        Publication requester = em.find(Publication.class, requesterPubId);

        //TODO: Chequear que las publicaciones existan y que no sean del mismo usuario.

        final Exchange exchange = new Exchange(0, offerer, requester, ExchangeState.PENDING, acceptCode, false, false, startDate, null);
        em.persist(exchange);

        return exchange;
    }

    @Override
    public Exchange rejectExchange(int acceptCode) {
        return null;
    }

    @Override
    public void setEndDate(int acceptCode, Timestamp endDate) {

    }

    @Override
    public Exchange acceptExchange(int acceptCode) {
        return null;
    }

    @Override
    public Exchange confirmOfferer(int acceptCode) {
        return null;
    }

    @Override
    public Exchange confirmRequester(int acceptCode) {
        return null;
    }

    @Override
    public void updateExchangeStatus(int acceptCode, int newStatus) {

    }

    @Override
    public Exchange findByAcceptCode(int acceptCode) throws ExchangeNotFoundException {
        return null;
    }

    @Override
    public Exchange getExchangeById(long exchangeId) {
        return null;
    }

    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer) {
        return null;
    }
}
