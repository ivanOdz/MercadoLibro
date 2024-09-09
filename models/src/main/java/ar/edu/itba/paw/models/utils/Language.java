package ar.edu.itba.paw.models.utils;

public enum Language {
    SPANISH(0),
    ENGLISH(1);

    private final int value;

    Language(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
