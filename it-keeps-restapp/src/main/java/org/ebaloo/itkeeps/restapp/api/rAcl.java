package org.ebaloo.itkeeps.restapp.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.domain.vertex.fAcl;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rAcl {

    @Context
    SecurityContext securityContext;

	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_ACL_GET_ID + "{id}")
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    public Response readId(@PathParam("id") Rid rid) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jAcl acl = fAcl.read(requesteurRid, rid);
    	return Response.ok().entity(acl).build();
    }
	
    
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_UPDATE)
    public Response update(final jAcl j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	fAcl.update(requesteurRid, j);
     	return Response.ok().entity(fAcl.read(requesteurRid, j.getRid())).build();
    }

    
    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_CREATE)
    public Response create(final jAcl j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jAcl acl = fAcl.create(requesteurRid, j);
    	return Response.ok().entity(fAcl.read(requesteurRid, acl.getRid())).build();
    }

    
    @DELETE //DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_DELETE + "{rid}")
    public Response delete(@PathParam("rid") Rid rid) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jAcl j = fAcl.delete(requesteurRid, rid);
    	return Response.ok().entity(j).build();
    }
}
