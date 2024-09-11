package ar.edu.itba.paw.models.utils;

public class LanguageWrapper {

    private Language language;
    private String displayName;

    public LanguageWrapper(Language language, String displayName) {

        this.language = language;
        this.displayName = displayName;
    }

    public Language getLanguage() {

        return language;
    }

    public String getDisplayName() {

        return displayName;
    }
}
