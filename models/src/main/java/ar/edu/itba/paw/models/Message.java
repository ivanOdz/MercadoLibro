package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_messageid_seq")
    @SequenceGenerator(sequenceName = "message_messageid_seq", name = "message_messageid_seq", allocationSize = 1)
    @Column(name = "messageId")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "exchangeId")
    private Exchange exchange;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "messageTime")
    private Timestamp messageTime;

    @Column(name = "message")
    private String message;

    public Message() {
        // only for JPA
    }

    public Message(Long messageId, Exchange exchange, User user, Timestamp messageTime, String message) {
        this.messageId = messageId;
        this.exchange = exchange;
        this.user = user;
        this.messageTime = messageTime;
        this.message = message;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setExchange(Exchange exchangeId) {
        this.exchange = exchangeId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public String getMessage() {
        return message;
    }

}
