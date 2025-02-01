package ar.edu.itba.paw.webapp.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
	
	@Context
    private UriInfo uriInfo;
	
	@Override
	public Response toResponse(BadRequestException exception) {
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getMessage());
		
		return Response.status(Response.Status.BAD_REQUEST)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}