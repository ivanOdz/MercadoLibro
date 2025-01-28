package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

public class CacheResponseUtil {

    public static final int maxAge = 31536000; // 1 year

    public static Response conditionalCacheResponse(Request request, EntityTag entityTag) {
        CacheControl cc = new CacheControl();
        cc.setMustRevalidate(true);
        Response.ResponseBuilder builder = request.evaluatePreconditions(entityTag);
        if (builder == null) {
            builder = Response.ok().tag(entityTag);
        }
        return builder.cacheControl(cc).build();
    }

    public static Response unconditionalCacheResponse(Response.ResponseBuilder response) {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(maxAge);
        return response.cacheControl(cc).build();
    }
}
