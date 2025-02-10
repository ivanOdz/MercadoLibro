package ar.edu.itba.paw.webapp.dto.output;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.UriInfo;

public class ErrorDTO {
	
    private String message;
    private Date time;
    private URI self;
    private int status;

    public static ErrorDTO fromErrorDTO(UriInfo uriInfo, String message, int status) {
    	
    	ErrorDTO dto = new ErrorDTO();
    	
    	dto.message = message;
    	dto.time = new Date();
    	dto.self = uriInfo.getAbsolutePath();
    	dto.status = status;
    	
    	return dto;
    }
    
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public void setTime(Date time) {
    	this.time = time;
    }
    
    public void setSelf(URI self) {
    	this.self = self;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }
    
    public String getMessage() {
    	return message;
    }
    
    public Date getTime() {
    	return time;
    }
    
    public URI getSelf() {
    	return self;
    }
    
    public int getStatus() {
    	return status;
    }
}
