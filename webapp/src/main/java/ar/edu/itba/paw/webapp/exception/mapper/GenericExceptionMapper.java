package ar.edu.itba.paw.webapp.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
	
	@Context
    private UriInfo uriInfo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Exception.class);
	private static final String DEFAULT_MESSAGE = "Internal Server Error.";
	
	@Override
	public Response toResponse(Exception exception) {
		
		String message = Optional.ofNullable(exception.getMessage()).filter(msg -> !msg.isEmpty()).orElse(DEFAULT_MESSAGE);
		LOGGER.error("Exception ({}) : {}", exception.getClass().getName(), message);
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, "Internal Server Error: " + exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}