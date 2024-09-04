package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeDao exchangeDao;

    public ExchangeServiceImpl(final ExchangeDao exchangeDao){
        this.exchangeDao = exchangeDao;
    }

    @Override
    public void acceptExchange(long acceptCode){
        exchangeDao.updateExchangeStatus(acceptCode, 0);
    }

    @Override
    public void rejectExchange(long acceptCode) {
        exchangeDao.updateExchangeStatus(acceptCode, 1);
    }

    @Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    @Override
    public long getId(long acceptCode) {
        return exchangeDao.getIdByAcceptCode(acceptCode);
    }

    @Override
    public String exchange(long acceptCode, boolean state) {
        switch (exchangeDao.exchange(acceptCode, state)){
            case ResponseState.ACCEPTED: {
                return "exchange/accepted";
            }
            case ResponseState.REJECTED: {
                return "exchange/rejected";
            }
            default: return "exchange/invalid";
        }
    }

    @Override
    public Exchange initializeExchange(boolean isForExchange, long offererId, long requesterId) {
//        if(isForExchange) {
            Random random = new Random();
            long acceptCode = random.nextLong();
            return exchangeDao.createExchange(offererId, requesterId, acceptCode);
//        }
//        return null;
    }
}
