package ar.edu.itba.paw.models.utils;

public enum SortType {
    RATING_ASCENDING,
    RATING_DESCENDING,
    PUBLICATION_DATE_ASCENDING,
    PUBLICATION_DATE_DESCENDING,
    BOOK_NAME_ASCENDING,
    BOOK_NAME_DESCENDING;


    public static SortType fromInt(int i) {
        try {
            return SortType.values()[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static SortType fromString(String sortTypeFilter) {
        for (SortType sortType : SortType.values()) {
            if (sortType.name().equalsIgnoreCase(sortTypeFilter)) {
                return sortType;
            }
        }
        return null;
    }

}
