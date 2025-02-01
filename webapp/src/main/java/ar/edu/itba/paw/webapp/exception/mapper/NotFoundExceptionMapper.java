package ar.edu.itba.paw.webapp.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
	
	@Context
    private UriInfo uriInfo;
	
	@Override
	public Response toResponse(NotFoundException exception) {
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getMessage());
		
		return Response.status(Response.Status.NOT_FOUND)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}