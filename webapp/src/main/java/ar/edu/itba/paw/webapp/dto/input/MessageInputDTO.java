package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class MessageInputDTO {
    private String message;
    private URI userURN;

    public Long getUserId() {
        return UrnResolverUtil.getUserId(userURN);
    }

    public String getMessage() {
        return message;
    }

    public URI getUserURN() {
        return userURN;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserURN(URI userURN) {
        this.userURN = userURN;
    }
}
