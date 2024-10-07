package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeService {

    void initializeExchange(long bookId, String location, long offererPubId);

    String exchange(int acceptCode, boolean state);

    /**
     * confirms that the offerer received the book
     */
    void cofirmOfferer(int acceptCode);

    /**
     * confirms that the requester received the book
     */
    void cofirmRequester(int acceptCode);

    Exchange getExchangeByAcceptCode(int acceptCode);
    
    Exchange getExchangeById(long exchangeId);
    
    PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState);

    PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState);
}
