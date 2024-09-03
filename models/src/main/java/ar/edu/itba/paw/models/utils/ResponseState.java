package ar.edu.itba.paw.models.utils;


public enum ResponseState {
    REJECTED(0),
    ACCEPTED(1),
    INVALID(2);

    private final int value;

    ResponseState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
