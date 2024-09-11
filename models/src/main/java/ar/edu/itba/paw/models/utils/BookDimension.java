package ar.edu.itba.paw.models.utils;

public enum BookDimension {
    SMALL(0),
    MEDIUM(1),
    LARGE(2);

    private final int value;

    BookDimension(int value) {
        this.value = value;
    }

    public static BookDimension fromInt(int i) {
        for (BookDimension d : BookDimension.values()) {
            if (d.getValue() == i) {
                return d;
            }
        }
        return null;
    }

    public int getValue() {

        return this.value;
    }

    public String getKey() {

        return "dimension." + name();
    }
}
