package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;

import javax.ws.rs.core.UriInfo;
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

    public static <P extends Metadata> Map<String, String> serializePaginationHeaders(P metadata, String baseUrl) {
        Map<String, String> headers = new HashMap<>();

        int currentPage = metadata.getCurrentPage();
        int maxPage = metadata.getMaxPage();

        StringBuilder linkHeader = new StringBuilder();

        if (currentPage > 1) {
            linkHeader.append("<").append(baseUrl).append("?page=1>; rel=\"first\", ");
            linkHeader.append("<").append(baseUrl).append("?page=").append(currentPage - 1).append(">; rel=\"prev\", ");
        }

        if (currentPage < maxPage) {
            linkHeader.append("<").append(baseUrl).append("?page=").append(currentPage + 1).append(">; rel=\"next\", ");
            linkHeader.append("<").append(baseUrl).append("?page=").append(maxPage).append(">; rel=\"last\"");
        }

        if (!linkHeader.isEmpty()) {
            headers.put("link", linkHeader.toString());
        }

        return headers;
    }

}
