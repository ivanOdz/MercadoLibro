package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ExchangeState;

import java.util.List;

public interface ExchangeDao {
	
    void updateExchangeStatus(int acceptCode, int newStatus);

//    Optional<Exchange> findById(long id);

//    long getIdByAcceptCode(int acceptCode);

    //ResponseState exchange(int acceptCode, boolean state);

//    Exchange createExchange(long offererId, long requesterId, int acceptCode, Timestamp startDate);

    List<Exchange> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, boolean isOfferer);

//    List<Exchange> getExchangesWhereUserIdIsRequester(long anUserId);

    void confirmRequester(int acceptCode);
    
    void confirmOfferer(int acceptCode);

}
