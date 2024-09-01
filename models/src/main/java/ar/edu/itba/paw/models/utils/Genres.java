package ar.edu.itba.paw.models.utils;

public enum Genres {
    FICTION(1, "Fiction"),
    NON_FICTION(2, "Non-Fiction"),
    MYSTERY(3, "Mystery"),
    THRILLER(4, "Thriller"),
    SCIENCE_FICTION(5, "Science Fiction"),
    FANTASY(6, "Fantasy"),
    ROMANCE(7, "Romance"),
    HISTORICAL_FICTION(8, "Historical Fiction"),
    HORROR(9, "Horror"),
    BIOGRAPHY(10, "Biography"),
    AUTOBIOGRAPHY(11, "Autobiography"),
    MEMOIR(12, "Memoir"),
    YOUNG_ADULT(13, "Young Adult"),
    CHILDRENS_LITERATURE(14, "Children's Literature"),
    GRAPHIC_NOVEL(15, "Graphic Novel"),
    CLASSIC(16, "Classic"),
    ADVENTURE(17, "Adventure"),
    DYSTOPIAN(18, "Dystopian"),
    SELF_HELP(19, "Self-Help"),
    POETRY(20, "Poetry"),
    LITERARY_FICTION(21, "Literary Fiction"),
    CRIME(22, "Crime"),
    WESTERN(23, "Western"),
    CONTEMPORARY(24, "Contemporary"),
    RELIGIOUS_SPIRITUAL(25, "Religious/Spiritual"),
    PHILOSOPHY(26, "Philosophy"),
    SCIENCE(27, "Science"),
    TRAVEL(28, "Travel"),
    TRUE_CRIME(29, "True Crime"),
    HISTORICAL_NON_FICTION(30, "Historical Non-Fiction");

    private final int value;
    private final String name;

    Genres(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
