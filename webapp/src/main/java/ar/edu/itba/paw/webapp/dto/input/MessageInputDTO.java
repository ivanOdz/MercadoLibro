package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class MessageInputDTO {
    private String message;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(URI userUrn) {
        this.userId = UrnResolverUtil.getUserId(userUrn);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
