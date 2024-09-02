package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Exchange;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ExchangeService {
    void acceptExchange(long acceptCode);
    void rejectExchange(long acceptCode);
    Exchange getExchangeById(long exchangeId);
    int getId(long acceptCode);
}
