
package org.ebaloo.itkeeps.restapp.tools;

import java.time.Instant;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.ApiPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("")
public class PingEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(PingEndpoint.class.getName());

	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.TOOLS_PING)
    @Timed
    public Response getPing() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("getPing()");

	   		return Response.ok(new Pong()).build();
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
    
}