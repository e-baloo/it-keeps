
package org.ebaloo.itkeeps.core.restapp.test;

import java.time.Instant;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed.SecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path(TestConfig.PATH)
public class TestEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(TestEndpoint.class.getName());

    @Context
    SecurityContext securityContext;

    
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApplicationRolesAllowed(SecurityRole.GUEST)
    @Path("ping")
    public Response getSecurePing() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("getPing()");

   		return Response.ok(new Pong()).build();
    }

    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static final class Pong {
    	
    	private static final String RESULT = "pong";
    	
    	@JsonProperty("result")
    	private final Object result = RESULT;
    	
    	@JsonProperty("now")
    	private final Object now = Instant.now().toString();
    	
    	private Pong() {
    		
        	if(logger.isTraceEnabled())
        		logger.trace("Pong.new()");
        	
    	}
    }
    
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("exception") 
    @PermitAll 
    @Timed()
    public Response getException() {
    	
    	throw new RuntimeException(new IllegalArgumentException("test exception") );
    	
    }
    

}