package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

public class Exchange {
    private long id;
    private long offerer;
    private long requester;
    private int state;
    private int acceptCode;


    public Exchange(long id, long offerer, long requester, int state, int acceptCode) {
        this.id = id;
        this.offerer = offerer;
        this.requester = requester;
        this.state = state;
        this.acceptCode = acceptCode;
    }

    public long getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public int getAcceptCode() {
        return acceptCode;
    }

    public long getOfferer() {
        return offerer;
    }

    public long getRequester() {
        return requester;
    }
}
