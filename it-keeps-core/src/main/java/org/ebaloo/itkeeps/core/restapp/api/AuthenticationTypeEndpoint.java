package org.ebaloo.itkeeps.core.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.api.AuthenticationType;
import org.ebaloo.itkeeps.core.tools.SecurityRole;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/authenticationType")
public class AuthenticationTypeEndpoint {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed(SecurityRole.ROOT)
    @Timed
    public Response get() {

		List<String> list = new ArrayList<String>();
		
		for(AuthenticationType at : AuthenticationType.values()) {
		    	list.add(at.toString());
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
}