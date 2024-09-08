package ar.edu.itba.paw.models.utils;

public enum ExchangeState {
    PENDING(0),
    ACCEPTED(1), // TODO: EL ACCEPTED FUNCIONA COMO TERMINATED
    REJECTED(2),
    TERMINATED(3);

    private final int value;

    ExchangeState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
