package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.CompleteBook;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.ExchangeWrapper;
import ar.edu.itba.paw.models.utils.ExchangeState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExchangeService {
    //Optional<Exchange> getExchangeById(long exchangeId);

    //long getId(int acceptCode);

    //String exchange(int acceptCode, boolean state);

    //void initializeExchange(CompleteBook requesterCompleteData, long offererPubId);

    //List<ExchangeWrapper> getExchangeRequesterWrapperListByUserId(long userId);

    List<Exchange> getExchangeRequesterListByUserId(long userId, ExchangeState exchangeState);


    List<Exchange> getExchangeOffererListByUserId(long userId, ExchangeState exchangeState);

    /**
     * confirms that the offerer received the book
     * @param acceptCode
     */
    //void cofirmOfferer(int acceptCode);

    /**
     * confirms that the requester received the book
     * @param acceptCode
     */
    //void cofirmRequester(int acceptCode);

}
