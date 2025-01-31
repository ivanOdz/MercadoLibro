package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationUtils {

    public static Map<String, String> serializeGenreWrapper(List<GenreWrapper> genreWrappers) {
        Map<String, String> headers = new HashMap<>();

        for (GenreWrapper genre : genreWrappers) {
            String headerKey = "X-Genre-" + genre.getGenre().getValue();
            String headerValue = genre.getGenre().getValue() + "=" + genre.getResultByGenre();
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    public static  Map<String, String> serializeConditionWrapper(List<BookStateWrapper> bookStateWrappers) {
        Map<String, String> headers = new HashMap<>();

        for (BookStateWrapper condition : bookStateWrappers) {
            String headerKey = "X-Condition-" + condition.getBookState().getValue();
            String headerValue = condition.getBookState().getValue() + "=" + condition.getResultByState();
            headers.put(headerKey, headerValue);
        }
        return headers;
    }
}
