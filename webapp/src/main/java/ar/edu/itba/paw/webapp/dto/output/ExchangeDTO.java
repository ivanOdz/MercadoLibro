package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Exchange;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;

public class ExchangeDTO {
    private String state;
    private Long accept_code;
    private Boolean offerer_received;
    private Boolean requester_received;
    private Boolean isConfirmed;
    private Timestamp start_date;
    private Timestamp end_date;

    private URI self;
    private URI offerer;
    private URI requester;
    private URI chat;

    private URI offererReview;
    private URI requesterReview;


    static public ExchangeDTO fromExchange(UriInfo uriInfo, Exchange exchange){
        ExchangeDTO dto = new ExchangeDTO();
        dto.state = exchange.getState().toString();
        dto.accept_code = exchange.getAcceptCode();
        dto.offerer_received = exchange.isOffererReceivedBook();
        dto.requester_received = exchange.isRequesterReceivedBook();
        dto.start_date = exchange.getExchangeStartDate();
        dto.end_date = exchange.getExchangeEndDate();
        dto.isConfirmed = exchange.isConfirmed();

        dto.self = uriInfo.getBaseUriBuilder().path("exchanges").path(String.valueOf(exchange.getExchangeId())).build();
        dto.offerer = uriInfo.getBaseUriBuilder().path("publications").path(String.valueOf(exchange.getOfferer().getPublicationId())).build();
        dto.requester = uriInfo.getBaseUriBuilder().path("publications").path(String.valueOf(exchange.getRequester().getPublicationId())).build();
        dto.chat = uriInfo.getBaseUriBuilder().path("exchanges").path(String.valueOf(exchange.getExchangeId())).path("messages").build();

        // /users/{offererReview}/reviews/{idReview} o null si no hay hecha una review.
        dto.offererReview = exchange.getIsReviewable() ? uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(exchange.getOfferer().getUser().getUserId())).path("reviews").path(String.valueOf(exchange.getOfferer().getUser().getUserId())).build() : null;
        dto.requesterReview = exchange.getIsReviewable() ? uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(exchange.getRequester().getUser().getUserId())).path("reviews").path(String.valueOf(exchange.getRequester().getUser().getUserId())).build() : null;

        return dto;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAcceptCode() {
        return accept_code;
    }

    public void setAcceptCode(Long accept_code) {
        this.accept_code = accept_code;
    }

    public Boolean getOfferer_received() {
        return offerer_received;
    }

    public void setOfferer_received(Boolean offerer_received) {
        this.offerer_received = offerer_received;
    }

    public Boolean getRequester_received() {
        return requester_received;
    }

    public void setRequester_received(Boolean requester_received) {
        this.requester_received = requester_received;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getOfferer() {
        return offerer;
    }

    public void setOfferer(URI offerer) {
        this.offerer = offerer;
    }

    public URI getRequester() {
        return requester;
    }

    public void setRequester(URI requester) {
        this.requester = requester;
    }

    public URI getChat() {
        return chat;
    }

    public void setChat(URI chat) {
        this.chat = chat;
    }

    public URI getOffererReview() {
        return offererReview;
    }

    public URI getRequesterReview() {
        return requesterReview;
    }

    public void setOffererReview(URI offererReview) {
        this.offererReview = offererReview;
    }

    public void setRequesterReview(URI requesterReview) {
        this.requesterReview = requesterReview;
    }
}
