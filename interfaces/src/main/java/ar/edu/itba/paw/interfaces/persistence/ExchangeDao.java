package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ResponseState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ExchangeDao {
    void updateExchangeStatus(int acceptCode, int newStatus);

    Optional<Exchange> findById(long id);

    long getIdByAcceptCode(int acceptCode);

    ResponseState exchange(int acceptCode, boolean state);

    Exchange createExchange(long offererId, long requesterId, int acceptCode, Timestamp startDate);

    List<Exchange> getExchangesByUserIdInvolved(long anUserId);

}
