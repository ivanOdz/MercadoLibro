package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ExchangeService {
    Optional<Exchange> getExchangeById(long exchangeId);

    long getId(int acceptCode);

    String exchange(int acceptCode, boolean state);
    Exchange initializeExchange(boolean isForExchange, long requesterId, long offererId);
}
