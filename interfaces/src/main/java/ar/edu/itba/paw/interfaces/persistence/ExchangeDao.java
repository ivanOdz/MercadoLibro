package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ExchangeDao {

    Optional<Exchange> createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate);

    Optional<Exchange> rejectExchange(int acceptCode);
    
    void setEndDate(int acceptCode, Timestamp endDate);

    Optional<Exchange> acceptExchange(int acceptCode);


    Optional<Exchange> confirmOfferer(int acceptCode);

    Optional<Exchange> confirmRequester(int acceptCode);

    void updateExchangeStatus(int acceptCode, int newStatus);

    Optional<Exchange> findByAcceptCode(int acceptCode);

    Optional<Exchange> getExchangeById(long exchangeId);

    PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer);
}
