package ar.edu.itba.paw.webapp.dto.output;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.core.UriInfo;

public class ErrorDTO {
	
    private String message;
    private Timestamp time;
    private URI self;

    public static ErrorDTO fromErrorDTO(UriInfo uriInfo, String message) {
    	
    	ErrorDTO dto = new ErrorDTO();
    	
    	dto.message = message;
    	dto.time = new Timestamp(new Date().getTime());
    	dto.self = uriInfo.getAbsolutePath();
    	
    	return dto;
    }
    
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public void setTime(Timestamp time) {
    	this.time = time;
    }
    
    public void setSelf(URI self) {
    	this.self = self;
    }
    
    public String getMessage() {
    	return message;
    }
    
    public Timestamp getTime() {
    	return time;
    }
    
    public URI getSelf() {
    	return self;
    }
}
