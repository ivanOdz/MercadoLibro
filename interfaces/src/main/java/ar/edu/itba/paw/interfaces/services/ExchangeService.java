package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.CompleteBook;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.ExchangeOffererWrapper;
import ar.edu.itba.paw.models.ExchangeRequesterWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExchangeService {
    Optional<Exchange> getExchangeById(long exchangeId);

    long getId(int acceptCode);

    String exchange(int acceptCode, boolean state);

    void initializeExchange(CompleteBook requesterCompleteData, long offererPubId);

    List<ExchangeRequesterWrapper> getExchangeRequesterWrapperListByUserId(long userId);

    //List<ExchangeOffererWrapper> getExchangeOffererWrapperListByUserId(long userId);
}
