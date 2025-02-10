package ar.edu.itba.paw.webapp.exceptionMapper;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationRuntimeException> {

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionMapper.class);

    @Override
    public Response toResponse(ApplicationRuntimeException exception) {
        LOGGER.error("Intercepted Exception: {} - {}", exception.getClass().getName(), exception.getExceptionMessage());

        ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getExceptionMessage(), exception.getStatusCode());

        return Response.status(exception.getStatusCode())
                .entity(errorDTO)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
