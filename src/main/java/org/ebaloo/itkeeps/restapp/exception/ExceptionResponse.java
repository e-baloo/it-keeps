package org.ebaloo.itkeeps.restapp.exception;

import java.time.Instant;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public final class ExceptionResponse {
	
	private final int status;

	private final String message;

	private final String now = Instant.now().toString();


	public ExceptionResponse(Response.Status status) {
		
		this.status = status.getStatusCode();
		this.message = "HTTP " + status.getStatusCode() + " " + status.getReasonPhrase();
		
	}

	public ExceptionResponse(int status, String message) {
		
		this.status = status;
		this.message = message;
		
	}
	
	
	public final Response getResponse() {
		
		return Response
			.status(this.status)
            .entity(this.toString())
            .type(MediaType.TEXT_PLAIN)
            .build();
		
	}
	
	public String toString() {
		return this.message + " @" + this.now;
	}
	
	
}