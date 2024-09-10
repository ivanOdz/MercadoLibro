package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.ExchangeWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExchangeService {
    Optional<Exchange> getExchangeById(long exchangeId);

    long getId(int acceptCode);

    String exchange(int acceptCode, boolean state);

    Exchange initializeExchange(long requesterId, long offererId);

    List<ExchangeWrapper> getExchangeWrapperListByUserId(long userId);
}
