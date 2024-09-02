package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Exchange;

import java.util.Optional;

public interface ExchangeDao {
    void updateExchangeStatus(long exchangeId, int newStatus);

    Exchange findById(long exchangeId);
}
