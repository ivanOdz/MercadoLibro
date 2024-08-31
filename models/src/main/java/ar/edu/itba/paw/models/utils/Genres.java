package ar.edu.itba.paw.models.utils;

public enum Genres {
    FICTION,
    NON_FICTION,
    MYSTERY,
    THRILLER,
    SCIENCE_FICTION,
    FANTASY,
    ROMANCE,
    HISTORICAL_FICTION,
    HORROR,
    BIOGRAPHY,
    AUTOBIOGRAPHY,
    MEMOIR,
    YOUNG_ADULT,
    CHILDRENS_LITERATURE,
    GRAPHIC_NOVEL,
    CLASSIC,
    ADVENTURE,
    DYSTOPIAN,
    SELF_HELP,
    POETRY,
    LITERARY_FICTION,
    CRIME,
    WESTERN,
    CONTEMPORARY,
    RELIGIOUS_SPIRITUAL,
    PHILOSOPHY,
    SCIENCE,
    TRAVEL,
    TRUE_CRIME,
    HISTORICAL_NON_FICTION;

    public static Genres fromInt(int i) {
        return Genres.values()[i];
    }
}

