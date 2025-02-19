package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.util.List;
import java.util.Optional;

public interface ExchangeService {

    Exchange initializeExchange(Long bookId, Long locationId, Long offererPubId);

    Optional<Exchange> getExchangeByAcceptCode(int acceptCode);

    Exchange getExchangeById(long exchangeId);
    
    PaginatedResponse<Exchange, BasicMetadata> getExchanges(long userId, ExchangeState exchangeState, Boolean isOfferer, Boolean isRequester, int currentPage);

    Message createMessage(long exchangeId, Long userId, String message);

    List<Message> getMessages(long exchangeId);

    Message getMessage(long messageId);

    void updateExchange(Integer acceptCode, Boolean accepted, Boolean requester);

}
