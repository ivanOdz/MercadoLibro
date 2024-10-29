package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.exceptions.ExchangeNotFoundException;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.sql.Timestamp;

public interface ExchangeDao {

    Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate);

    Exchange rejectExchange(int acceptCode);
    
    void setEndDate(int acceptCode, Timestamp endDate);

    Exchange acceptExchange(int acceptCode);

    Exchange confirmOfferer(int acceptCode);

    Exchange confirmRequester(int acceptCode);

    void updateExchangeStatus(int acceptCode, ExchangeState newStatus);

    Exchange findByAcceptCode(int acceptCode) throws ExchangeNotFoundException;

    Exchange getExchangeById(long exchangeId);

    PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer);
}
