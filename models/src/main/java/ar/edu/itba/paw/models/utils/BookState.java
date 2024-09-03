package ar.edu.itba.paw.models.utils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BookState {
	
	NEW(1, "New", "Nuevo"),
	LIKE_NEW(2, "Like New", "Como Nuevo"),
	VERY_GOOD(3, "Very Good", "Muy Bueno"),
	GOOD(4, "Very Good", "Bueno"),
	ACCEPTABLE(5, "Acceptable", "Aceptable"),
	WORN(6, "Worn", "Usado");
	
	private final int value;
	private final String englishName;
	private final String spanishName;
	
	BookState(int value, String englishName, String spanishName) {
		
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
		
		return Stream.of(BookState.values()).map(bookState -> bookState.getName(locale)).collect(Collectors.toList());
	}

    public static BookState fromInt(int i) {
    	
        return BookState.values()[i];
    }
    
    public int getValue() {
    	
    	return this.value;
    }
}