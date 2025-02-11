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
    
	public static BookDimension fromString(String dimensionFilter) {
		for (BookDimension dimension : BookDimension.values()) {
			if (dimension.value.equalsIgnoreCase(dimensionFilter)) {
				return dimension;
			}
		}
		return null;
	}
}
