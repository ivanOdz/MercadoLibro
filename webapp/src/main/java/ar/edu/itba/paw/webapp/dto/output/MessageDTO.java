package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class MessageDTO {
    private String message;
    private Date time;

    private URI self;
    private URI user;
    private URI exchange;

    static public MessageDTO fromMessage(UriInfo uriInfo, Message message){
        MessageDTO dto = new MessageDTO();
        dto.message = message.getMessage();
        dto.time =  message.getMessageTime() != null ? new Date(message.getMessageTime().getTime()) : null;
        dto.self = uriInfo.getBaseUriBuilder().path("exchange").path(String.valueOf(message.getExchange().getExchangeId())).path("messages").path(String.valueOf(message.getMessageId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getUser().getUserId())).build();
        dto.exchange = uriInfo.getBaseUriBuilder().path("exchange").path(String.valueOf(message.getExchange().getExchangeId())).build();
        return dto;
    }

    public Date getTime() {
        return time;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getExchange() {
        return exchange;
    }

    public void setExchange(URI exchange) {
        this.exchange = exchange;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return UrnResolverUtil.getUserId(user);
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
