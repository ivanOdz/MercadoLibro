package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

import java.util.Random;

public class Exchange {
    private final long exchangeId;
    private final long publicationId1;
    private final long publicationId2;
    private ExchangeState exchangeState;
    private final long acceptCode;

    public Exchange(long exchangeId, long publicationId1, long publicationId2) {
        this.exchangeId = exchangeId;
        this.publicationId1 = publicationId1;
        this.publicationId2 = publicationId2;
        exchangeState = ExchangeState.PENDING;
        acceptCode = generateAcceptCode();
    }

    public long generateAcceptCode() {
        Random random = new Random();
        return (long) random.nextInt(99999);
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public long getPublicationId() {
        return publicationId1;
    }

    public long getPublicationId2() {
        return publicationId2;
    }

    public ExchangeState getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(ExchangeState exchangeState) {
        this.exchangeState = exchangeState;
    }
}
