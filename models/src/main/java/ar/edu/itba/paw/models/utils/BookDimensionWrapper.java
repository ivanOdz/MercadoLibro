package ar.edu.itba.paw.models.utils;

public class BookDimensionWrapper {

    private BookDimension dimension;
    private String displayName;

    public BookDimensionWrapper(BookDimension dim, String displayName) {
        this.dimension = dim;
        this.displayName = displayName;
    }

    public BookDimension getDimension() {
        return dimension;
    }

    public String getDisplayName() {
        return displayName;
    }
}
