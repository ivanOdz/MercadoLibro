package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationUtils {

    public static Map<String, String> serializeGenreWrapper(List<GenreWrapper> genreWrappers) {
        Map<String, String> headers = new HashMap<>();

        for (GenreWrapper genre : genreWrappers) {
            String headerKey = "X-" + genre.getGenre().getValue().replace(".", "-");
            String headerValue = genre.getGenre().getValue() + "=" + genre.getResultByGenre();
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    public static  Map<String, String> serializeConditionWrapper(List<BookStateWrapper> bookStateWrappers) {
        Map<String, String> headers = new HashMap<>();

        for (BookStateWrapper condition : bookStateWrappers) {
            String headerKey = "X-" + condition.getBookState().getValue().replace(".", "-");
            String headerValue = condition.getBookState().getValue() + "=" + condition.getResultByState();
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    public static <P extends Metadata> Map<String, String> serializePaginationHeaders(P metadata) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Total-Count", String.valueOf(metadata.getTotalResults()));
        headers.put("X-Total-Pages", String.valueOf(metadata.getMaxPage()));
        headers.put("X-Current-Page", String.valueOf(metadata.getCurrentPage()));
        return headers;
    }
}
