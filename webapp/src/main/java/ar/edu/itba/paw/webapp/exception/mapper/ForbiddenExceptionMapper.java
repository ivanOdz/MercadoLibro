package ar.edu.itba.paw.webapp.exception.mapper;

import java.util.Optional;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.paw.interfaces.exceptions.base.ForbiddenException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
	
	@Context
    private UriInfo uriInfo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForbiddenException.class);
	private static final String DEFAULT_MESSAGE = "Forbidden.";
	
	@Override
	public Response toResponse(ForbiddenException exception) {
		
		String message = Optional.ofNullable(exception.getExceptionMessage()).filter(msg -> !msg.isEmpty()).orElse(DEFAULT_MESSAGE);
		LOGGER.error("Exception ({}) : {}", exception.getClass().getName(), message);
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getExceptionMessage(), exception.getStatusCode());
		
		return Response.status(Response.Status.FORBIDDEN)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}