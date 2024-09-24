package ar.edu.itba.paw.models;

import java.util.List;

import ar.edu.itba.paw.models.utils.ExchangeState;

import java.time.LocalDateTime;

public class ExchangeWrapper {
    private LocalDateTime exchangeStartDate;
    private String acceptCode;
    private String requesterLocation;
    private String requesterMail;
    private String requesterUsername;
    private Long requesterProfileImageId;
    private Long offererProfileImageId;
    private String offererLocation;
    private String offererMail;
    private String offererUsername;
    private String offererBookState;
    private String requesterBookState;
    private String offererBookTitle;
    private String requesterBookTitle;
    private Long reqBookImageId;
    private Long offBookImageId;
    private String requesterBookAuthors;
    private String offererBookAuthors;
    private Boolean offererReceivedBook;
    private Boolean requesterReceivedBook;

    // Constructor con todos los campos
    public ExchangeWrapper(LocalDateTime exchangeStartDate, String acceptCode, String requesterLocation, String requesterMail,
                       String requesterUsername, Long requesterProfileImageId, Long offererProfileImageId,
                       String offererLocation, String offererMail, String offererUsername, String offererBookState,
                       String requesterBookState, String offererBookTitle, String requesterBookTitle,
                       Long reqBookImageId, Long offBookImageId, String requesterBookAuthors, String offererBookAuthors,
                       Boolean offererReceivedBook, Boolean requesterReceivedBook) {
        this.exchangeStartDate = exchangeStartDate;
        this.acceptCode = acceptCode;
        this.requesterLocation = requesterLocation;
        this.requesterMail = requesterMail;
        this.requesterUsername = requesterUsername;
        this.requesterProfileImageId = requesterProfileImageId;
        this.offererProfileImageId = offererProfileImageId;
        this.offererLocation = offererLocation;
        this.offererMail = offererMail;
        this.offererUsername = offererUsername;
        this.offererBookState = offererBookState;
        this.requesterBookState = requesterBookState;
        this.offererBookTitle = offererBookTitle;
        this.requesterBookTitle = requesterBookTitle;
        this.reqBookImageId = reqBookImageId;
        this.offBookImageId = offBookImageId;
        this.requesterBookAuthors = requesterBookAuthors;
        this.offererBookAuthors = offererBookAuthors;
        this.offererReceivedBook = offererReceivedBook;
        this.requesterReceivedBook = requesterReceivedBook;
    }

    // Getters (Opcional si los necesitas)
    public LocalDateTime getExchangeStartDate() {
        return exchangeStartDate;
    }

    public String getAcceptCode() {
        return acceptCode;
    }

    public String getRequesterLocation() {
        return requesterLocation;
    }

    public String getRequesterMail() {
        return requesterMail;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public Long getRequesterProfileImageId() {
        return requesterProfileImageId;
    }

    public Long getOffererProfileImageId() {
        return offererProfileImageId;
    }

    public String getOffererLocation() {
        return offererLocation;
    }

    public String getOffererMail() {
        return offererMail;
    }

    public String getOffererUsername() {
        return offererUsername;
    }

    public String getOffererBookState() {
        return offererBookState;
    }

    public String getRequesterBookState() {
        return requesterBookState;
    }

    public String getOffererBookTitle() {
        return offererBookTitle;
    }

    public String getRequesterBookTitle() {
        return requesterBookTitle;
    }

    public Long getReqBookImageId() {
        return reqBookImageId;
    }

    public Long getOffBookImageId() {
        return offBookImageId;
    }

    public String getRequesterBookAuthors() {
        return requesterBookAuthors;
    }

    public String getOffererBookAuthors() {
        return offererBookAuthors;
    }

    public Boolean getOffererReceivedBook() {
        return offererReceivedBook;
    }

    public Boolean getRequesterReceivedBook() {
        return requesterReceivedBook;
    }
}
