package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import java.util.List;

public interface ExchangeService {

    Exchange initializeExchange(long bookId, long locationId, long offererPubId);

    boolean exchange(int acceptCode, boolean state);

    /**
     * confirms that the offerer received the book
     */
    void confirmOffer(long userId, int acceptCode);

    /**
     * confirms that the requester received the book
     */
    void confirmRequest(long userId, int acceptCode);

    Exchange getExchangeByAcceptCode(int acceptCode);
    
    Exchange getExchangeById(long exchangeId);
    
//    PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState);
//
//    PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState);

    PaginatedResponse<Exchange, BasicMetadata> getExchanges(long userId, ExchangeState exchangeState, Boolean isOfferer, Boolean isRequester, int currentPage);

    void createMessage(long exchangeId, User user, String message);

    List<Message> getMessages(long exchangeId);

}
