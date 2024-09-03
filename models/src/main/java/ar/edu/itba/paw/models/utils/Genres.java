package ar.edu.itba.paw.models.utils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Genres {
	
	FICTION(1, "Fiction", "Ficción"),
	NON_FICTION(2, "Non-Fiction", "No Ficción"),
	MYSTERY(3, "Mystery", "Misterio"),
	THRILLER(4, "Thriller", "Suspenso"),
	SCIENCE_FICTION(5, "Science Fiction", "Ciencia Ficción"),
	FANTASY(6, "Fantasy", "Fantasía"),
	ROMANCE(7, "Romance", "Romance"),
	HISTORICAL_FICTION(8, "Historical Fiction", "Ficción Histórica"),
	HORROR(9, "Horror", "Horror"),
	BIOGRAPHY(10, "Biography", "Biografía"),
	AUTOBIOGRAPHY(11, "Autobiography", "Autobiografía"),
	MEMOIR(12, "Memoir", "Memorias"),
	YOUNG_ADULT(13, "Young Adult", "Juvenil"),
	CHILDRENS_LITERATURE(14, "Children's Literature", "Literatura Infantil"),
	GRAPHIC_NOVEL(15, "Graphic Novel", "Novela Gráfica"),
	CLASSIC(16, "Classic", "Clásico"),
	ADVENTURE(17, "Adventure", "Aventura"),
	DYSTOPIAN(18, "Dystopian", "Distopía"),
	SELF_HELP(19, "Self-Help", "Autoayuda"),
	POETRY(20, "Poetry", "Poesía"),
	LITERARY_FICTION(21, "Literary Fiction", "Ficción Literaria"),
	CRIME(22, "Crime", "Crimen"),
	WESTERN(23, "Western", "Oeste"),
	CONTEMPORARY(24, "Contemporary", "Contemporáneo"),
	RELIGIOUS_SPIRITUAL(25, "Religious/Spiritual", "Religioso/Espiritual"),
	PHILOSOPHY(26, "Philosophy", "Filosofía"),
	SCIENCE(27, "Science", "Ciencia"),
	TRAVEL(28, "Travel", "Viajes"),
	TRUE_CRIME(29, "True Crime", "Crimen Verdadero"),
	HISTORICAL_NON_FICTION(30, "Historical Non-Fiction", "No Ficción Histórica");
	
	private final int value;
	private final String englishName;
	private final String spanishName;
	
	Genres(int value, String englishName, String spanishName) {
		
		this.value = value;
		this.englishName = englishName;
		this.spanishName = spanishName;
    }
	
	public String getName(Locale locale) {
		
		if (locale.getLanguage().equals("es")) {
			return spanishName;
		} else {
			return englishName;
		}
	}
    
	public static List<String> getNames(Locale locale) {
		
		return Stream.of(Genres.values()).map(genre -> genre.getName(locale)).collect(Collectors.toList());
	}
	
	public static Genres fromInt(int i) {
		
		return Genres.values()[i];
	}
	
	public int getValue() {
		
		return value;
	}
}
