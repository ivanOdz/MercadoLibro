package ar.edu.itba.paw.models.utils;

public enum Genre {

	FICTION(1),
	NON_FICTION(2),
	MYSTERY(3),
	THRILLER(4),
	SCIENCE_FICTION(5),
	FANTASY(6),
	ROMANCE(7),
	HISTORICAL_FICTION(8),
	HORROR(9),
	BIOGRAPHY(10),
	AUTOBIOGRAPHY(11),
	MEMOIR(12),
	YOUNG_ADULT(13),
	CHILDRENS_LITERATURE(14),
	GRAPHIC_NOVEL(15),
	CLASSIC(16),
	ADVENTURE(17),
	DYSTOPIAN(18),
	SELF_HELP(19),
	POETRY(20),
	LITERARY_FICTION(21),
	CRIME(22),
	WESTERN(23),
	CONTEMPORARY(24),
	RELIGIOUS_SPIRITUAL(25),
	PHILOSOPHY(26),
	SCIENCE(27),
	TRAVEL(28),
	TRUE_CRIME(29),
	HISTORICAL_NON_FICTION(30),
	OTHER(31);
	
	private final int value;

	Genre(int value) {
		
		this.value = value;
    }
	
	public static Genre fromInt(int i) {
		
		return Genre.values()[i];
	}
	
	public int getValue() {
		
		return value;
	}
	
	public String getKey() {
		
		return "genre." + name();
	}
}
