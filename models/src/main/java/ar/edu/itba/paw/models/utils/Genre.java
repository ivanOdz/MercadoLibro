package ar.edu.itba.paw.models.utils;

public enum Genre {

	FICTION(0),
	NON_FICTION(1),
	MYSTERY(2),
	THRILLER(3),
	SCIENCE_FICTION(4),
	FANTASY(5),
	ROMANCE(6),
	HISTORICAL_FICTION(7),
	HORROR(8),
	BIOGRAPHY(9),
	AUTOBIOGRAPHY(10),
	MEMOIR(11),
	YOUNG_ADULT(12),
	CHILDRENS_LITERATURE(13),
	GRAPHIC_NOVEL(14),
	CLASSIC(15),
	ADVENTURE(16),
	DYSTOPIAN(17),
	SELF_HELP(18),
	POETRY(19),
	LITERARY_FICTION(20),
	CRIME(21),
	WESTERN(22),
	CONTEMPORARY(23),
	RELIGIOUS_SPIRITUAL(24),
	PHILOSOPHY(25),
	SCIENCE(26),
	TRAVEL(27),
	TRUE_CRIME(28),
	HISTORICAL_NON_FICTION(29),
	OTHER(30);
	
	private final int value;

	Genre(int value) {
		
		this.value = value;
    }
	
	public static Genre fromInt(int i) {
		try {
			return Genre.values()[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getValue() {
		
		return value;
	}

	public static Genre fromString(String genreFilter) {
		for (Genre genre : Genre.values()) {
			if (genre.name().equalsIgnoreCase(genreFilter)) {
				return genre;
			}
		}
		return null;
	}


}
