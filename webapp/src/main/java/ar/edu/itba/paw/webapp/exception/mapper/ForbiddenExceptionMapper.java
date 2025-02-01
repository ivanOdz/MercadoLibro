import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

import ar.edu.itba.paw.interfaces.exceptions.base.ForbiddenException;
import ar.edu.itba.paw.webapp.dto.output.ErrorDTO;

public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
	
	@Context
    private UriInfo uriInfo;
	
	@Override
	public Response toResponse(ForbiddenException exception) {
		
		ErrorDTO errorDTO = ErrorDTO.fromErrorDTO(uriInfo, exception.getMessage(), Response.Status.FORBIDDEN.getStatusCode());
		
		return Response.status(Response.Status.FORBIDDEN)
						.entity(errorDTO)
						.type(MediaType.APPLICATION_JSON)
						.build();
	}
}