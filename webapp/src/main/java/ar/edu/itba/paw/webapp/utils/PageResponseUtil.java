package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class PageResponseUtil {

    public static Response getResponse(int page, int maxPage, UriInfo uriInfo, Response.ResponseBuilder builder) {

        builder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 0).build(), "first");
        builder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPage).build(), "last");

        if (maxPage > 0) {

            if (page > 0 && page <= maxPage) {
                builder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
            }

            if (page >= 0 && page < maxPage) {
                builder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
            }
        }

        return builder.build();
    }
}
