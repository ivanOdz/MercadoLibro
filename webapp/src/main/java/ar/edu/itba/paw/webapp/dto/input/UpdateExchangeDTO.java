package ar.edu.itba.paw.webapp.dto.input;

import javax.ws.rs.QueryParam;

public class UpdateExchangeDTO {
    private Integer acceptCode;
    private Boolean accepted;
    private Boolean requester;

    public Integer getAcceptCode() {
        return acceptCode;
    }

    public Boolean getRequester() {
        return requester;
    }

    public void setRequester(Boolean requester) {
        this.requester = requester;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public void setAcceptCode(Integer acceptCode) {
        this.acceptCode = acceptCode;
    }
}
