package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ExchangeService {
    void acceptExchange(long acceptCode);
    void rejectExchange(long acceptCode);
    Optional<Exchange> getExchangeById(long exchangeId);

    long getId(int acceptCode);

    String exchange(int acceptCode, boolean state);
    Exchange initializeExchange(boolean isForExchange, long offererId, long requesterId);

}
