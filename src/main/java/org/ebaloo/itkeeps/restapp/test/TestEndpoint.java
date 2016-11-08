
package org.ebaloo.itkeeps.restapp.test;

import java.time.Instant;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKZXJzZXktU2VjdXJpdHktQmFzaWMiLCJzdWIiOiJtYXJjIiwiZXhwIjoxNDc4NTUyMTQ3LCJpYXQiOjE0Nzg1NDg1NDcsImp0aSI6IjAifQ.K_RjWoMWeBo4_LlFvDTDg56311Nf_UE7rBjUp3m-r9Q
 * 
 * curl http://127.0.0.1:8080/test/ping -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKZXJzZXktU2VjdXJpdHktQmFzaWMiLCJzdWIiOiJtYXJjIiwiZXhwIjoxNDc4NTUyMTQ3LCJpYXQiOjE0Nzg1NDg1NDcsImp0aSI6IjAifQ.K_RjWoMWeBo4_LlFvDTDg56311Nf_UE7rBjUp3m-r9Q"
 * 
 * 
 * 
 */


@Path(TestResourceConfig.PATH)
public class TestEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(TestEndpoint.class.getName());

	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ping")
    public Response getPing() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("getPing()");

    	try {
	   		return Response.ok(new Pong()).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	    }      

    }

    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static final class Pong {
    	
    	private static final String RESULT = "pong";
    	
    	@JsonProperty("result")
    	private final Object o1 = RESULT;
    	
    	@JsonProperty("now")
    	private final Object o2 = Instant.now().toString();
    	
    	private Pong() {
    		
        	if(logger.isTraceEnabled())
        		logger.trace("Pong.new()");
        	
    	}
    }
    
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("exception") 
    @PermitAll 
    public Response getException() {
    	
    	throw new RuntimeException(new IllegalArgumentException("test exception") );
    	
    }
    

}