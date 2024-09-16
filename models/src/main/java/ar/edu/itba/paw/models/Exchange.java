package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

import java.sql.Timestamp;

public class Exchange {
    private final long exchangeId;

    public boolean isOffererReceivedBook() {
        return offererReceivedBook;
    }

    public boolean isRequesterReceivedBook() {
        return requesterReceivedBook;
    }

    public Timestamp getExchangeStartDate() {
        return exchangeStartDate;
    }

    public Timestamp getExchangeEndDate() {
        return exchangeEndDate;
    }

    private final long offererPubId;
    private final long requesterPubId;
    private final ExchangeState exchangeState;
    private final int acceptCode;
    private final boolean offererReceivedBook;
    private final boolean requesterReceivedBook;
    private final Timestamp exchangeStartDate;
    private final Timestamp exchangeEndDate;

    public Exchange(long exchangeId, long offererPubId, long requesterPubId, ExchangeState exchangeState, int acceptCode,
                    boolean offererReceivedBook, boolean requesterReceivedBook, Timestamp exchangeStartDate, Timestamp exchangeEndDate) {
        this.exchangeId = exchangeId;
        this.offererPubId = offererPubId;
        this.requesterPubId = requesterPubId;
        this.exchangeState = exchangeState;
        this.acceptCode = acceptCode;
        this.offererReceivedBook = offererReceivedBook;
        this.requesterReceivedBook = requesterReceivedBook;
        this.exchangeStartDate = exchangeStartDate;
        this.exchangeEndDate = exchangeEndDate;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public long getOffererPubId() {
        return offererPubId;
    }

    public long getRequesterPubId() {
        return requesterPubId;
    }

    public ExchangeState getExchangeState() {
        return exchangeState;
    }

    public int getAcceptCode() {
        return acceptCode;
    }

    public boolean isConfirmed(){
        return offererReceivedBook && requesterReceivedBook;
    }

}
