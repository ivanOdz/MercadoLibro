package ar.edu.itba.paw.webapp.dto.input;

import java.net.URI;

public class MessageInputDTO {
    private String message;
    private URI userUrn;

    public URI getUserUrn() {
        return userUrn;
    }

    public void setUserUrn(URI userUrn) {
        this.userUrn = userUrn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
