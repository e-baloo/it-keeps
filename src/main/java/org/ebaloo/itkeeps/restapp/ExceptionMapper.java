package org.ebaloo.itkeeps.restapp;

import java.time.Instant;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.ebaloo.itkeeps.restapp.authentication.JwtFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

	private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class.getName());

	


	@Override
	public Response toResponse(Exception exception) {
		
		logger.error(null, exception);
		
	    Response response;

	    if (exception instanceof WebApplicationException) {
	    
	    	WebApplicationException webEx = (WebApplicationException)exception;
	    	response = new ExceptionResponse(webEx.getResponse().getStatus(), webEx.getMessage()).getResponse();

	    } else {

	    	response = new ExceptionResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "HTTP " + Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() + " " + Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()).getResponse();

	    }
	    return response;	
	   }

	
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static final class ExceptionResponse {
    	
    	@JsonProperty("status")
    	private final int status;

    	@JsonProperty("message")
    	private final String message;

    	@JsonProperty("now")
    	private final String now = Instant.now().toString();

    	
    	private ExceptionResponse(int status, String message) {
    		
    		this.status = status;
    		this.message = message;
    		
    	}
    	
    	
    	private final Response getResponse() {
    		
    		return Response
				.status(this.status)
	            .entity(this)
	            .type(MediaType.APPLICATION_JSON)
	            .build();
    		
    	}
    	
    	
    }
	
}