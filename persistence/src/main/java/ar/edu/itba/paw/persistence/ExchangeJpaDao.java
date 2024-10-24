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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;

import static ar.edu.itba.paw.models.utils.Constants.BOOKS_PAGE_SIZE;
import static ar.edu.itba.paw.models.utils.Constants.EXCHANGES_PAGE_SIZE;

@Primary
@Repository
public class ExchangeJpaDao implements ExchangeDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate) {
        Publication offerer = em.find(Publication.class, offererPubId);
        Publication requester = em.find(Publication.class, requesterPubId);

        //TODO: Chequear que las publicaciones existan y que no sean del mismo usuario.

        final Exchange exchange = new Exchange(null, offerer, requester, ExchangeState.PENDING, acceptCode, false, false, startDate, null);
        em.persist(exchange);

        return exchange;
    }

    @Override
    public Exchange rejectExchange(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.REJECTED);
        return exchange;
    }

    @Override
    public void setEndDate(int acceptCode, Timestamp endDate) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeEndDate(endDate);
    }

    @Override
    public Exchange acceptExchange(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.ACCEPTED);
        return exchange;
    }

    @Override
    public Exchange confirmOfferer(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setOffererReceivedBook(true);
        return exchange;
    }

    @Override
    public Exchange confirmRequester(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setRequesterReceivedBook(true);
        return exchange;
    }

    @Override
    public void updateExchangeStatus(int acceptCode, int newStatus) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.fromInt(newStatus));
    }

    @Override
    public Exchange findByAcceptCode(int acceptCode) throws ExchangeNotFoundException {
        Exchange exchange = em.find(Exchange.class, acceptCode);
        return exchange;
    }

    @Override
    public Exchange getExchangeById(long exchangeId) {
        Exchange exchange = em.find(Exchange.class, exchangeId);
        // exception if exchange is null
        return exchange;
    }

    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer) {
        if(currentPage < 0){
            currentPage = 0;
        }

        StringBuilder queryString = new StringBuilder("SELECT e.exchangeId FROM Exchange e WHERE ");

        if (isOfferer) {
            queryString.append("e.offerer.userId = :userId");
        } else {
            queryString.append("e.requester.userId = :userId");
        }

        Query nativeQuery = em.createQuery(queryString.toString());
        nativeQuery.setMaxResults(EXCHANGES_PAGE_SIZE);
        nativeQuery.setFirstResult(currentPage * EXCHANGES_PAGE_SIZE);

        List<Long> exchangeIds = nativeQuery.getResultList();

        TypedQuery<Exchange> query = em.createQuery("FROM Exchange e WHERE e.exchangeId IN (:ids) AND e.state = :state", Exchange.class);
        query.setParameter("ids", exchangeIds);

        return new PaginatedResponse<>(query.getResultList(), new BasicMetadata(currentPage, EXCHANGES_PAGE_SIZE, exchangeIds.size()));
    }
}
