package ar.edu.itba.paw.models.utils;

public enum ExchangeState {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    TERMINATED("TERMINATED");

    private final String value;

    ExchangeState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
