package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

public class CacheResponseUtil {

    public static final int MAX_AGE = 31536000; // 1 year

    public static Response.ResponseBuilder conditionalCacheResponse(Request request, EntityTag entityTag, Object entity) {
        CacheControl cc = new CacheControl();
        cc.setMustRevalidate(true);
        Response.ResponseBuilder builder = request.evaluatePreconditions(entityTag);
        if (builder == null) {
            builder = Response.ok(entity).tag(entityTag);
        }
        return builder.cacheControl(cc);
    }

    public static Response unconditionalCacheResponse(Response.ResponseBuilder responseBuilder) {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(MAX_AGE);
        return responseBuilder.cacheControl(cc).build();
    }
}
