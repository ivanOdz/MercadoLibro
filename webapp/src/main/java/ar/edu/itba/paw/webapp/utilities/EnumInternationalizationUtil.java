package ar.edu.itba.paw.webapp.utilities;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EnumInternationalizationUtil {

    private EnumInternationalizationUtil() {
        // To prevent instantiation
    }

    public static List<LocalizedEnumWrapper<BookStateWrapper>> localizeBookStateWrappers(List<BookStateWrapper> bookStateWrappers) {
        List<LocalizedEnumWrapper<BookStateWrapper>> localizedList = new ArrayList<>();
        for (BookStateWrapper bookStateWrapper : bookStateWrappers) {
            String i18nDisplayName = generateI18nKey(bookStateWrapper.getBookState());
            localizedList.add(new LocalizedEnumWrapper<>(bookStateWrapper, i18nDisplayName));
        }
        return localizedList;
    }

    public static List<LocalizedEnumWrapper<GenreWrapper>> localizeGenreWrappers(List<GenreWrapper> genreWrappers) {
        List<LocalizedEnumWrapper<GenreWrapper>> localizedList = new ArrayList<>();
        for (GenreWrapper genreWrapper : genreWrappers) {
            String i18nDisplayName = generateI18nKey(genreWrapper.getGenre());
            localizedList.add(new LocalizedEnumWrapper<>(genreWrapper, i18nDisplayName));
        }
        return localizedList;
    }

    public static List<LocalizedEnumWrapper<Genre>> getLocalizedGenres() {
        return Stream.of(Genre.values())
                .map(genre -> new LocalizedEnumWrapper<>(genre, generateI18nKey(genre)))
                .collect(Collectors.toList());
    }

    public static List<LocalizedEnumWrapper<BookState>> getLocalizedBookStates() {
        return Stream.of(BookState.values())
                .map(bookState -> new LocalizedEnumWrapper<>(bookState, generateI18nKey(bookState)))
                .collect(Collectors.toList());
    }


    private static String generateI18nKey(Enum<?> enumValue) {
        if (enumValue == null) {
            return "";
        }
        String enumNameFormatted = enumValue.name().toLowerCase().replace('_', '.');
        return enumValue.getClass().getSimpleName().toLowerCase() + "." + enumNameFormatted;
    }
}
