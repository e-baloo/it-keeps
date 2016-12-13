package org.ebaloo.itkeeps.restapp.api;


import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jEnumAcl;
import org.ebaloo.itkeeps.api.model.jEnumAuthentication;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rEnum {

    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ENUM_ACL)
    public Response enumAcl() {
    	return Response.ok().entity(new jEnumAcl()).build();
    }
	
    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.API_ENUM_AUTH)
    public Response enumAuth() {
    	return Response.ok().entity(new jEnumAuthentication()).build();
    }
    
}
