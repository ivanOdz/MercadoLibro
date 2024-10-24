package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "exchange")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchanges_exchangeid_seq")
    @SequenceGenerator(sequenceName = "exchanges_exchangeid_seq", name = "exchanges_exchangeid_seq", allocationSize = 1)
    @Column(name = "exchangeid")
    private Long exchangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offererPubId", nullable = false)
    private Publication offerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requesterPubId", nullable = false)
    private Publication requester;

    @Column(name = "exchangeState", nullable = false)
    private ExchangeState state;

    @Column(name = "acceptCode", nullable = false)
    private long acceptCode;

    @Column(name = "offererReceivedBook")
    private boolean offererReceivedBook;

    @Column(name = "requesterReceivedBook")
    private boolean requesterReceivedBook;

    @Column(name = "exchangeStartDate")
    private Timestamp exchangeStartDate;

    @Column(name = "exchangeEndDate")
    private Timestamp exchangeEndDate;

    private boolean isReviewable;

    public Exchange() {
        // only for JPA
    }

    public Exchange(Long exchangeId, Publication offerer, Publication requester, ExchangeState state, long acceptCode, boolean offererReceivedBook, boolean requesterReceivedBook, Timestamp exchangeStartDate, Timestamp exchangeEndDate) {
        this.exchangeId = exchangeId;
        this.offerer = offerer;
        this.requester = requester;
        this.state = state;
        this.acceptCode = acceptCode;
        this.offererReceivedBook = offererReceivedBook;
        this.requesterReceivedBook = requesterReceivedBook;
        this.isReviewable = state.getValue() != ExchangeState.REJECTED.getValue() && state.getValue() != ExchangeState.PENDING.getValue();
        this.exchangeStartDate = exchangeStartDate;
        this.exchangeEndDate = exchangeEndDate;
    }

    public ExchangeState getState() {
        return state;
    }

    public boolean getIsReviewable() {
        return isReviewable;
    }

    public boolean isOffererReceivedBook() {
        return offererReceivedBook;
    }

    public boolean isRequesterReceivedBook() {
        return requesterReceivedBook;
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

    public Publication getRequester() {
        return requester;
    }

    public Publication getOfferer() {
        return offerer;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public boolean isConfirmed(){
        return offererReceivedBook && requesterReceivedBook;
    }

    public ExchangeState getExchangeState() {
        return state;
    }

    public void setExchangeState(ExchangeState state) {
        this.state = state;
    }

    public void setExchangeEndDate(Timestamp exchangeEndDate) {
        this.exchangeEndDate = exchangeEndDate;
    }

    public void setOffererReceivedBook(boolean offererReceivedBook) {
        this.offererReceivedBook = offererReceivedBook;
    }

    public void setRequesterReceivedBook(boolean requesterReceivedBook) {
        this.requesterReceivedBook = requesterReceivedBook;
    }
}


