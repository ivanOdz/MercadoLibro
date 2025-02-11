package ar.edu.itba.paw.models.utils;

public enum Language {
    SPANISH("language.spanish"),
    ENGLISH("language.english");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    
	public static Language fromString(String languageFilter) {
		for (Language language : Language.values()) {
			if (language.value.equalsIgnoreCase(languageFilter)) {
				return language;
			}
		}
		return null;
	}
}
