package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Message;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;

public class MessagesDTO {
    private String message;
    private Timestamp time;

    private URI self;
    private URI user;
    private URI exchange;

    public MessagesDTO() {
    }

    static public MessagesDTO fromMessage(UriInfo uriInfo, Message message){
        MessagesDTO dto = new MessagesDTO();
        dto.message = message.getMessage();
        dto.time = message.getMessageTime();
        dto.self = uriInfo.getBaseUriBuilder().path("exchange").path(String.valueOf(message.getExchange().getExchangeId())).path("messages").path(String.valueOf(message.getMessageId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getUser().getUserId())).build();
        dto.exchange = uriInfo.getBaseUriBuilder().path("exchange").path(String.valueOf(message.getExchange().getExchangeId())).build();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
