package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        final Exchange exchange = new Exchange(null, offerer, requester, ExchangeState.PENDING, acceptCode, false, false, new Timestamp(startDate.getTime()), null, new ArrayList<>());
        
        em.persist(exchange);
        
        return exchange;
    }

    @Override
    public void rejectExchange(Exchange exchange, int acceptCode) {
        exchange.setExchangeState(ExchangeState.REJECTED);
    }

    @Override
    public void setEndDate(Exchange exchange, int acceptCode, Timestamp endDate) {
        exchange.setExchangeEndDate(endDate);
    }

    @Override
    public void acceptExchange(Exchange exchange, int acceptCode){
        exchange.setExchangeState(ExchangeState.ACCEPTED);
    }

    @Override
    public void confirmOfferer(Exchange exchange, int acceptCode) {
        exchange.setOffererReceivedBook(true);
    }

    @Override
    public void confirmRequester(Exchange exchange, int acceptCode) {
        exchange.setRequesterReceivedBook(true);
    }

    @Override
    public void updateExchangeStatus(Exchange exchange, int acceptCode, ExchangeState newStatus) {
        exchange.setExchangeState(newStatus);
    }

    @Override
    public Optional<Exchange> findByAcceptCode(int acceptCode) {
        TypedQuery<Exchange> exchange = em.createQuery("FROM Exchange e WHERE e.acceptCode = :acceptCode", Exchange.class);
        exchange.setParameter("acceptCode", acceptCode);
        return Optional.ofNullable(exchange.getSingleResult());
    }

    @Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
        return Optional.ofNullable(em.find(Exchange.class, exchangeId));
    }

    @Override
    public PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int page, boolean isOfferer, boolean isRequester) {
        if (page < 0) {
            page = 0;
        }

        StringBuilder queryString = new StringBuilder("SELECT e.exchangeId FROM exchange e JOIN publication p ON ");
        
        if(isOfferer && isRequester) {
            queryString.append("(p.publicationId = e.offererpubId OR p.publicationId = e.requesterpubId)");
        } else if (isOfferer) {
            queryString.append("p.publicationId = e.offererpubId");
        } else {
            queryString.append("p.publicationId = e.requesterpubId");
        }
        
        queryString.append(" WHERE p.userId = :userId AND e.exchangestate = :state");

        Query nativeQuery = em.createNativeQuery(queryString.toString());
        nativeQuery.setParameter("userId", anUserId);
        nativeQuery.setParameter("state", exchangeState.getValue());

        nativeQuery.setMaxResults(EXCHANGES_PAGE_SIZE);
        nativeQuery.setFirstResult(page * EXCHANGES_PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> exchangeIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        TypedQuery<Exchange> query = em.createQuery("FROM Exchange e WHERE e.exchangeId IN (:ids)", Exchange.class);
        query.setParameter("ids", exchangeIds);

        List<Exchange> exchanges = query.getResultList();

        return new PaginatedResponse<>(exchanges, new BasicMetadata(page, getTotalResultsByExchange(anUserId, exchangeState, isOfferer), EXCHANGES_PAGE_SIZE));
    }

    private int getTotalResultsByExchange(long anUserId, ExchangeState exchangeState, boolean isOfferer) {

        StringBuilder queryString = new StringBuilder("SELECT COUNT(*) FROM exchange e JOIN publication p ON p.publicationId = ");
        
        if (isOfferer) {
            queryString.append("e.offererpubId");
        } else {
            queryString.append("e.requesterpubId");
        }
        queryString.append(" WHERE p.userId = :userId AND e.exchangestate = :state");

        Query nativeQuery = em.createNativeQuery(queryString.toString());
        nativeQuery.setParameter("userId", anUserId);
        nativeQuery.setParameter("state", exchangeState.getValue());

        return ((Number) nativeQuery.getSingleResult()).intValue();
    }


    @Override
    public void createMessage(Exchange exchange, long userId, String message, Timestamp time) {
    	
        Message newMessage = new Message(null, exchange ,em.find(User.class, userId), time, message);
        em.persist(newMessage);
        exchange.getChat().add(newMessage);
    }

    @Override
    public Optional<Message> getMessageById(long messageId) {
        TypedQuery<Message> m = em.createQuery("FROM Message e WHERE e.messageId = :id", Message.class);
        m.setParameter("id", messageId);
        return Optional.ofNullable(em.find(Message.class, messageId));
    }
}
