package ar.edu.itba.paw.models.utils;

public enum BookDimension {
    SMALL("dimension.small"),
    MEDIUM("dimension.medium"),
    LARGE("dimension.large");

    private final String value;

    BookDimension(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
