
package org.ebaloo.itkeeps.restapp.test;

import java.time.Instant;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
//import javax.annotation.security.RolesAllowed;
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

import org.ebaloo.itkeeps.tools.SecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKZXJzZXktU2VjdXJpdHktQmFzaWMiLCJzdWIiOiJtYXJjIiwiZXhwIjoxNDc4NTUyMTQ3LCJpYXQiOjE0Nzg1NDg1NDcsImp0aSI6IjAifQ.K_RjWoMWeBo4_LlFvDTDg56311Nf_UE7rBjUp3m-r9Q
 * 
 * curl http://127.0.0.1:8080/test/ping -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJJVC1LZWVwcyIsImp0aSI6Im1hcmMiLCJzdWIiOiJtYXJjIiwiZXhwIjoxNDc4NjA1MTc5LCJpYXQiOjE0Nzg2MDQ1Nzl9.IoYfyqHqEDwL33SDMcELR7HfT6psZuPwXjq2irhE5L8"
 * 
 * 
 * 
 */


@Path(TestConfig.PATH)
public class TestEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(TestEndpoint.class.getName());

    @Context
    SecurityContext securityContext;

    
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({SecurityRole.GUEST, SecurityRole.USER, SecurityRole.ADMIN, SecurityRole.ROOT})
    @Path("ping")
    public Response getSecurePing() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("getPing()");

    	System.out.println("guid   : " + securityContext.getUserPrincipal().getName());
    	System.out.println("GUEST  : " + securityContext.isUserInRole("GUEST"));
    	System.out.println("USER   : " + securityContext.isUserInRole("USER"));
    	System.out.println("ADMIN  : " + securityContext.isUserInRole("ADMIN"));
    	System.out.println("ROOT   : " + securityContext.isUserInRole("ROOT"));
    	
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