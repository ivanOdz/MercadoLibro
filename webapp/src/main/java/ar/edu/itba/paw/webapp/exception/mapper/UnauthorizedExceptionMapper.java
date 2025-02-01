package ar.edu.itba.paw.webapp.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ar.edu.itba.paw.interfaces.exceptions.base.UnauthorizedException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

@Component
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
	
	@Context
    private UriInfo uriInfo;
	
	@Override
	public Response toResponse(UnauthorizedException exception) {
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getMessage());
		
		return Response.status(Response.Status.UNAUTHORIZED)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}