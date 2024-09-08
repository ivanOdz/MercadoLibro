package ar.edu.itba.paw.models.utils;

public enum PublicationState {
    TERMINATED(0),
    CURRENT(1),
    OFFERED(2);


    private final int value;

    PublicationState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PublicationState fromInt(int i) {
        return PublicationState.values()[i];
    }
}
