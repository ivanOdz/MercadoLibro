package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Exchange {
    private final long exchangeId;
    private final Publication offerer;
    private final Publication requester;
    private final ExchangeState state;
    private final long acceptCode;
    private final boolean offererReceivedBook;
    private final boolean requesterReceivedBook;
    private Timestamp exchangeStartDate;

    private Timestamp exchangeEndDate;

    public Exchange(long exchangeId, Publication offerer, Publication requester, ExchangeState state, long acceptCode, boolean offererReceivedBook, boolean requesterReceivedBook) {
        this.exchangeId = exchangeId;
        this.offerer = offerer;
        this.requester = requester;
        this.state = state;
        this.acceptCode = acceptCode;
        this.offererReceivedBook = offererReceivedBook;
        this.requesterReceivedBook = requesterReceivedBook;
    }

    public Timestamp getExchangeEndDate() {
        return exchangeEndDate;
    }

    public Timestamp getExchangeStartDate() {
        return exchangeStartDate;
    }

    public long getAcceptCode() {
        return acceptCode;
    }

    public ExchangeState getState() {
        return state;
    }

    public Publication getRequester() {
        return requester;
    }

    public Publication getOfferer() {
        return offerer;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public boolean isConfirmed(){
        return offererReceivedBook && requesterReceivedBook;
    }

}
