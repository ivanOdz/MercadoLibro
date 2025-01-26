package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;

import java.util.List;

public class SerializationUtils {

    public static String serializeGenreWrapper(List<GenreWrapper> genreWrappers) {
        StringBuilder rta = new StringBuilder();
        rta.append("[");
        for (int i = 0; i < genreWrappers.size(); i++) {
            GenreWrapper genreWrapper = genreWrappers.get(i);
            if (i > 0) {
                rta.append(", ");
            }
            rta.append("{\"genre\": \"")
                    .append(genreWrapper.getGenre().toString())
                    .append("\", \"amount\": ")
                    .append(genreWrapper.getResultByGenre())
                    .append("}");
        }
        rta.append("]");
        return rta.toString();
    }

    public static String serializeConditionWrapper(List<BookStateWrapper> bookStateWrappers) {
        StringBuilder rta = new StringBuilder();
        rta.append("[");
        for (int i = 0; i < bookStateWrappers.size(); i++) {
            BookStateWrapper bookStateWrapper = bookStateWrappers.get(i);
            if (i > 0) {
                rta.append(", ");
            }
            rta.append("{\"state\": \"")
                    .append(bookStateWrapper.getBookState().toString())
                    .append("\", \"amount\": ")
                    .append(bookStateWrapper.getResultByState())
                    .append("}");
        }
        rta.append("]");
        return rta.toString();
    }
}
