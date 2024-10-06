package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExchangeService {

    void initializeExchange(long bookId, String location, long offererPubId, User currentUser);

    String exchange(int acceptCode, boolean state);

    /**
     * confirms that the offerer received the book
     * @param acceptCode
     */
    void cofirmOfferer(int acceptCode);

    /**
     * confirms that the requester received the book
     * @param acceptCode
     */
    void cofirmRequester(int acceptCode);

    Optional<Exchange> getExchangeByAcceptCode(int acceptCode);
    
    Optional<Exchange> getExchangeById(long exchangeId);
    
    PaginatedResponse<Exchange, BasicMetadata> getExchangeOffererListByUserId(long userId, int currentPage, ExchangeState exchangeState);

    PaginatedResponse<Exchange, BasicMetadata> getExchangeRequesterListByUserId(long userId, int currentPage, ExchangeState exchangeState);
}
