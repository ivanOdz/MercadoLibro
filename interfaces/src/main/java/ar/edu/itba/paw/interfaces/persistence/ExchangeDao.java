package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.sql.Timestamp;
import java.util.Optional;

public interface ExchangeDao {

    Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate);

    void rejectExchange(Exchange exchange, int acceptCode);

    void setEndDate(Exchange exchange, int acceptCode, Timestamp endDate) ;

    void acceptExchange(Exchange exchange, int acceptCode);

    void confirmOfferer(Exchange exchange, int acceptCode);

    void confirmRequester(Exchange exchange, int acceptCode);

    void updateExchangeStatus(Exchange exchange, int acceptCode, ExchangeState newStatus);

    Optional<Exchange> findByAcceptCode(int acceptCode);

    Optional<Exchange> getExchangeById(long exchangeId);

    PaginatedResponse<Exchange, BasicMetadata> getAllExchangesByUserId(long anUserId, ExchangeState exchangeState, int currentPage, boolean isOfferer, boolean isRequester);

    void createMessage(Exchange exchange, long userId, String message, Timestamp time);

    Optional<Message> getMessageById(long messageId);
}
