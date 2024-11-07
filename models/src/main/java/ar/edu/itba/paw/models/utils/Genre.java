package ar.edu.itba.paw.models.utils;

public enum Genre {

	FICTION("genre.fiction"),
	NON_FICTION("genre.non.fiction"),
	MYSTERY("genre.mystery"),
	THRILLER("genre.thriller"),
	SCIENCE_FICTION("genre.science.fiction"),
	FANTASY("genre.fantasy"),
	ROMANCE("genre.romance"),
	HISTORICAL_FICTION("genre.historical.fiction"),
	HORROR("genre.horror"),
	BIOGRAPHY("genre.biography"),
	AUTOBIOGRAPHY("genre.autobiography"),
	MEMOIR("genre.memoir"),
	YOUNG_ADULT("genre.young.adult"),
	CHILDRENS_LITERATURE("genre.childrens.literature"),
	GRAPHIC_NOVEL("genre.graphic.novel"),
	CLASSIC("genre.classic"),
	ADVENTURE("genre.adventure"),
	DYSTOPIAN("genre.dystopian"),
	SELF_HELP("genre.self.help"),
	POETRY("genre.poetry"),
	LITERARY_FICTION("genre.literary.fiction"),
	CRIME("genre.crime"),
	WESTERN("genre.western"),
	CONTEMPORARY("genre.contemporary"),
	RELIGIOUS_SPIRITUAL("genre.religious.spiritual"),
	PHILOSOPHY("genre.philosophy"),
	SCIENCE("genre.science"),
	TRAVEL("genre.travel"),
	TRUE_CRIME("genre.true.crime"),
	HISTORICAL_NON_FICTION("genre.historical.non.fiction"),
	OTHER("genre.other");

	private final String value;

	Genre(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static Genre fromString(String genreFilter) {
		for (Genre genre : Genre.values()) {
			if (genre.value.equalsIgnoreCase(genreFilter)) {
				return genre;
			}
		}
		return null;
	}
}
