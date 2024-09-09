package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
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
    public Optional<Exchange> getExchangeById(long exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    @Override
    public long getId(int acceptCode) {
        return exchangeDao.getIdByAcceptCode(acceptCode);
    }

    @Override
    public String exchange(int acceptCode, boolean state) {
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
    public Exchange initializeExchange(boolean isForExchange, long requesterPubId, long offererPubId) {
//        if(isForExchange) {
            Random random = new Random();
            int acceptCode = Math.abs(random.nextInt());
            return exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode);
//        }
//        return null;
    }
}
