package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.utils.ExchangeState;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class PageResponseUtil {

    public static Response getResponse(int page, int maxPage, UriBuilder uri, Response.ResponseBuilder builder) {

        builder.link(uri.clone().replaceQueryParam("page", 0).build(), "first");
        builder.link(uri.clone().replaceQueryParam("page", maxPage).build(), "last");

        if (maxPage > 0) {

            if (page > 0 && page <= maxPage) {
                builder.link(uri.clone().replaceQueryParam("page", page - 1).build(), "prev");
            }

            if (page >= 0 && page < maxPage) {
                builder.link(uri.clone().replaceQueryParam("page", page + 1).build(), "next");
            }
        }
        return builder.build();
    }

    public static UriBuilder getUriBuilderBooks(UriBuilder uri, final long userId, final String search, final String sortType, final String state, final String genre) {
        uri.queryParam("owner", userId);
        if(search != null) {
            uri.queryParam("search", search);
        }
        if(sortType != null) {
            uri.queryParam("sort", sortType);
        }
        if(state != null) {
            uri.queryParam("state", state);
        }
        if(genre != null) {
            uri.queryParam("genre", genre);;
        }
        return uri;
    }

    public static UriBuilder getUriBuilderExchanges(UriBuilder uri, final long userId, final ExchangeState state, final Boolean isOfferer, final Boolean isRequester) {
        uri.queryParam("user_id", userId);
        if(state != null) {
            uri.queryParam("state", state);
        }
        if(isOfferer != null) {
            uri.queryParam("is_offerer", isOfferer);
        }
        if(isRequester != null) {
            uri.queryParam("is_requester", isRequester);;
        }
        return uri;
    }
}