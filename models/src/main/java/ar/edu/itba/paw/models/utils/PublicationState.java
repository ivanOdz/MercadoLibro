package ar.edu.itba.paw.models.utils;

public enum PublicationState {
    CURRENT(1),
    TERMINATED(0);

    private final int value;

    PublicationState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
