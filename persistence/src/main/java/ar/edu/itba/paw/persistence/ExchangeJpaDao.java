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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Transactional
    @Override
    public Exchange rejectExchange(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.REJECTED);
        return exchange;
    }

    @Transactional
    @Override
    public void setEndDate(int acceptCode, Timestamp endDate) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeEndDate(endDate);
    }

    @Transactional
    @Override
    public Exchange acceptExchange(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.ACCEPTED);
        return exchange;
    }

    @Transactional
    @Override
    public Exchange confirmOfferer(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setOffererReceivedBook(true);
        return exchange;
    }

    @Transactional
    @Override
    public Exchange confirmRequester(int acceptCode) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setRequesterReceivedBook(true);
        return exchange;
    }

    @Transactional
    @Override
    public void updateExchangeStatus(int acceptCode, int newStatus) {
        Exchange exchange = findByAcceptCode(acceptCode);
        exchange.setExchangeState(ExchangeState.fromInt(newStatus));
    }

    @Override
    public Exchange findByAcceptCode(int acceptCode) throws ExchangeNotFoundException {
        TypedQuery<Exchange> exchange = em.createQuery("FROM Exchange e WHERE e.acceptCode = :acceptCode", Exchange.class);
        exchange.setParameter("acceptCode", acceptCode);

        Exchange result = exchange.getSingleResult();
        if (result == null) {
            throw new ExchangeNotFoundException("Exchange not found");
        }
        return result;
    }

    @Override
    public Exchange getExchangeById(long exchangeId) {
        return em.find(Exchange.class, exchangeId);
        // exception if exchange is null
    }

    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer) {
        if(currentPage < 0){
            currentPage = 0;
        }

        StringBuilder queryString = new StringBuilder("SELECT e.exchangeId FROM exchange e JOIN publication p ON p.publicationId = ");
        if (isOfferer) {
            queryString.append("e.offererpubId");
        } else {
            queryString.append("e.requesterpubId");
        }
        queryString.append(" WHERE p.userId = :userId");

        Query nativeQuery = em.createNativeQuery(queryString.toString());
        nativeQuery.setParameter("userId", anUserId);

        nativeQuery.setMaxResults(EXCHANGES_PAGE_SIZE);
        nativeQuery.setFirstResult(currentPage * EXCHANGES_PAGE_SIZE);


        @SuppressWarnings("unchecked")
        List<Long> exchangeIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        TypedQuery<Exchange> query = em.createQuery("FROM Exchange e WHERE e.exchangeId IN (:ids) AND e.state = :state", Exchange.class);
        query.setParameter("ids", exchangeIds);
        query.setParameter("state", exchangeState);

        List<Exchange> exchanges = query.getResultList();

        return new PaginatedResponse<>(exchanges, new BasicMetadata(currentPage, EXCHANGES_PAGE_SIZE, exchangeIds.size()));
    }
}
