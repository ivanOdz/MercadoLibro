package ar.edu.itba.paw.models.utils;

public enum SortType {
    RATING_ASCENDING("sort.rating.ascending"),
    RATING_DESCENDING("sort.rating.descending"),
    PUBLICATION_DATE_ASCENDING("sort.publication.date.ascending"),
    PUBLICATION_DATE_DESCENDING("sort.publication.date.descending"),
    BOOK_NAME_ASCENDING("sort.book.name.ascending"),
    BOOK_NAME_DESCENDING("sort.book.name.descending");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public static SortType fromString(String sortTypeFilter) {
        for (SortType sortType : SortType.values()) {
            if (sortType.value.equalsIgnoreCase(sortTypeFilter)) {
                return sortType;
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }
}
