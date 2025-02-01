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

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.base.UnauthorizedException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
	
	@Context
    private UriInfo uriInfo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundException.class);
	private static final String DEFAULT_MESSAGE = "Not Found.";
	
	@Override
	public Response toResponse(NotFoundException exception) {
		
		String message = Optional.ofNullable(exception.getExceptionMessage()).filter(msg -> !msg.isEmpty()).orElse(DEFAULT_MESSAGE);
		LOGGER.error("Exception ({}) : {}", exception.getClass().getName(), message);
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getExceptionMessage(), exception.getStatusCode());
		
		return Response.status(Response.Status.NOT_FOUND)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}