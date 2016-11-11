package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.tools.SecurityFactory;
import org.ebaloo.itkeeps.tools.SecurityFactory.AuthenticationType;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/authenticationType")
public class AuthenticationTypeEndpoint {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Timed
    public Response get() {

		List<String> list = new ArrayList<String>();
		
		for(AuthenticationType at : SecurityFactory.AuthenticationType.values()) {
		    	list.add(at.toString());
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
}
