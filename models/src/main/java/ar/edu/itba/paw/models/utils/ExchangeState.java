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

    public static boolean equals(ExchangeState state, ExchangeState state2) {
        return state.getValue().equals(state2.getValue());
    }
}
