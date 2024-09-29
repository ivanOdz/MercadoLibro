package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.ResponseState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ExchangeDao {
	
    void updateExchangeStatus(int acceptCode, int newStatus);

//    Optional<Exchange> findById(long id);
    Optional<Exchange> findByAcceptCode(int acceptCode);

//    long getIdByAcceptCode(int acceptCode);

    ResponseState exchange(int acceptCode, boolean state);

    Optional<Exchange> createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate);

    List<Exchange> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, boolean isOfferer);

    void confirmRequester(int acceptCode);
    
    void confirmOfferer(int acceptCode);

    Optional<Exchange> getExchangeById(long exchangeId);


}
