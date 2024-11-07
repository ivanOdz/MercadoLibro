package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "exchange")
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchanges_exchangeid_seq")
    @SequenceGenerator(sequenceName = "exchanges_exchangeid_seq", name = "exchanges_exchangeid_seq", allocationSize = 1)
    @Column(name = "exchangeid")
    private Long exchangeId;

    @ManyToOne
    @JoinColumn(name = "offererPubId", nullable = false)
    private Publication offerer;

    @ManyToOne
    @JoinColumn(name = "requesterPubId", nullable = false)
    private Publication requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "exchangeState", nullable = false)
    private ExchangeState state;

    @Column(name = "acceptCode", nullable = false)
    private int acceptCode;

    @Column(name = "offererReceivedBook")
    private boolean offererReceivedBook;

    @Column(name = "requesterReceivedBook")
    private boolean requesterReceivedBook;

    @Column(name = "exchangeStartDate")
    private Timestamp exchangeStartDate;

    @Column(name = "exchangeEndDate")
    private Timestamp exchangeEndDate;


    @OneToMany(mappedBy = "exchange", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> chat;


    public Exchange() {
        // only for JPA
    }

    public Exchange(Long exchangeId, Publication offerer, Publication requester, ExchangeState state, int acceptCode, boolean offererReceivedBook, boolean requesterReceivedBook, Timestamp exchangeStartDate, Timestamp exchangeEndDate, List<Message> chat) {
        this.exchangeId = exchangeId;
        this.offerer = offerer;
        this.requester = requester;
        this.state = state;
        this.acceptCode = acceptCode;
        this.offererReceivedBook = offererReceivedBook;
        this.requesterReceivedBook = requesterReceivedBook;
        this.exchangeStartDate = exchangeStartDate;
        this.exchangeEndDate = exchangeEndDate;
        this.chat = chat;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void setOfferer(Publication offerer) {
        this.offerer = offerer;
    }

    public void setRequester(Publication requester) {
        this.requester = requester;
    }

    public void setState(ExchangeState state) {
        this.state = state;
    }

    public void setAcceptCode(int acceptCode) {
        this.acceptCode = acceptCode;
    }

    public void setExchangeStartDate(Timestamp exchangeStartDate) {
        this.exchangeStartDate = exchangeStartDate;
    }

    public List<Message> getChat() {
        return chat;
    }

    public void setChat(List<Message> chat) {
        this.chat = chat;
    }

    public ExchangeState getState() {
        return state;
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

    private boolean isReviewable() {
        return (state != ExchangeState.REJECTED && state != ExchangeState.PENDING);
    }

    public boolean getIsReviewable() {
        return isReviewable();
    }

    public boolean getChatAvailable() {
        return ExchangeState.equals(state, ExchangeState.ACCEPTED);
    }
}


