package ar.edu.itba.paw.models.utils;

public enum PublicationState {
    CURRENT,
    TERMINATED;

    public static PublicationState fromInt(int i) {
        return PublicationState.values()[i];
    }
}
