package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ResponseState;

import java.util.Optional;

public interface ExchangeDao {
    void updateExchangeStatus(long exchangeId, int newStatus);

    Optional<Exchange> findById(long id);

    long getIdByAcceptCode(long acceptCode);

    ResponseState exchange(long acceptCode, boolean state);

    Exchange createExchange(long offererId, long requesterId, long acceptCode);

}
