package ar.edu.itba.paw.webapp.utilities;

public class LocalizedEnumWrapper<T> {
    private final T enumWrapper;
    private final String i18nDisplayName;

    public LocalizedEnumWrapper(T enumWrapper, String i18nDisplayName) {
        this.enumWrapper = enumWrapper;
        this.i18nDisplayName = i18nDisplayName;
    }

    public T getEnumWrapper() {
        return enumWrapper;
    }

    public String getI18nDisplayName() {
        return i18nDisplayName;
    }
}
