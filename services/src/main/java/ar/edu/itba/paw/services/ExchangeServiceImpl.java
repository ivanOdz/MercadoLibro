package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.models.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeDao exchangeDao;

    public ExchangeServiceImpl(final ExchangeDao exchangeDao){
        this.exchangeDao = exchangeDao;
    }

    @Override
    public void acceptExchange(long acceptCode){
        exchangeDao.updateExchangeStatus(acceptCode, 1);
    }

    @Override
    public void rejectExchange(long acceptCode) {
        exchangeDao.updateExchangeStatus(acceptCode, 2);
    }

    @Override
    public Exchange getExchangeById(long exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    @Override
    public int getId(long acceptCode) {
        return 0;
    }
}
