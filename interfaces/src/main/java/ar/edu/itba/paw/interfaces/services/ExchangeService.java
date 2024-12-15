package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

public interface ExchangeService {

    void initializeExchange(long bookId, long locationId, long offererPubId);

    boolean exchange(int acceptCode, boolean state);

    /**
     * confirms that the offerer received the book
     */
    void cofirmOfferer(long userId, int acceptCode);

    /**
     * confirms that the requester received the book
     */
    void cofirmRequester(long userId, int acceptCode);

    Exchange getExchangeByAcceptCode(int acceptCode);
    
    Exchange getExchangeById(long exchangeId);
    
    PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, String currentPage, ExchangeState exchangeState);

    PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, String currentPage, ExchangeState exchangeState);

    void createMessage(long exchangeId, long userId, String message);

}
