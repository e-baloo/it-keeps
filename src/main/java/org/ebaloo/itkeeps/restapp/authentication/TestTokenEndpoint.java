
package org.ebaloo.itkeeps.restapp.authentication;

import java.time.Instant;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.restapp.tools.ToolsResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/testToken")
public class TestTokenEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(TestTokenEndpoint.class.getName());

	
	/*
	 * public ShoppingBasketResource get(@Context SecurityContext sc) {
    if (sc.isUserInRole("PreferredCustomer") {
        return new PreferredCustomerShoppingBasketResource();
    } else {
        return new ShoppingBasketResource();
    }
}

	 */
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response test() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("test()");

    	try {
	   		return Response.ok("ok").build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	    }      

    }

    
    
}