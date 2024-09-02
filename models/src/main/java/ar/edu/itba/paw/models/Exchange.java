package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.ExchangeState;

public class Exchange {
    private int id;
    private int p1;
    private int p2;
    private int state;
    private int acceptCode;

    public int getId() {
        return id;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getState() {
        return state;
    }

    public int getAcceptCode() {
        return acceptCode;
    }
}
