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

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.services.BookServiceImpl;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
	
	@Context
    private UriInfo uriInfo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BadRequestExceptionMapper.class);
	private static final String DEFAULT_MESSAGE = "Bad Resquest.";
	
	@Override
	public Response toResponse(BadRequestException exception) {
		
		String message = Optional.ofNullable(exception.getExceptionMessage()).filter(msg -> !msg.isEmpty()).orElse(DEFAULT_MESSAGE);
		LOGGER.error("Exception ({}) : {}", exception.getClass().getName(), message);
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getExceptionMessage(), exception.getStatusCode());
		
		return Response.status(Response.Status.BAD_REQUEST)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}