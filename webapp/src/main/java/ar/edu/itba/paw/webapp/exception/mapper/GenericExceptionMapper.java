package ar.edu.itba.paw.webapp.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
	
	@Context
    private UriInfo uriInfo;
	
	@Override
	public Response toResponse(Exception exception) {
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, "Internal Server Error: " + exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}