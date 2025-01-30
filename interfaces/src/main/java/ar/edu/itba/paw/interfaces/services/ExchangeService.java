package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.net.URI;
import java.util.List;

public interface ExchangeService {

    Exchange initializeExchange(URI book, URI location, URI offererPub);

    Exchange getExchangeByAcceptCode(int acceptCode);
    
    Exchange getExchangeById(long exchangeId);
    
//    PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState);
//
//    PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState);

    PaginatedResponse<Exchange, BasicMetadata> getExchanges(URI user, ExchangeState exchangeState, Boolean isOfferer, Boolean isRequester, int currentPage);

    void createMessage(long exchangeId, URI user, String message);

    List<Message> getMessages(long exchangeId);

    Message getMessage(long messageId);

    void updateExchange(Integer acceptCode, Boolean accepted, Boolean requester);

}
