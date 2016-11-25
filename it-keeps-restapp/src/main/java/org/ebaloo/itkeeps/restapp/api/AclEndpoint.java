package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
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
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.domain.vertex.vAcl;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class AclEndpoint {

    @Context
    SecurityContext securityContext;

	
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_GET_ALL)
    public Response readAll() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<jAcl> list = new ArrayList<jAcl>();
		
		for(vAcl acl : vAcl.getAllBase(null, vAcl.class, false)) {
	    	list.add(acl.read(requesteurGuid));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_ACL_GET_ID + "{id}")
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    public Response readId(@PathParam("id") String id) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vAcl acl = vAcl.get(null, vAcl.class, id, false);
		
		if(acl == null)
			throw new RuntimeException("readId(" + id + ") is null" );
		
		
    	return Response.ok().entity(acl.read(requesteurGuid)).build();
    }
	
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_UPDATE)
    public Response update(final jAcl j) {
    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	vAcl acl = vAcl.get(null, vAcl.class,  j.getGuid(), false);

    	acl.update(j, requesteurGuid);

    	return Response.ok().entity(acl.read(requesteurGuid)).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_CREATE)
    public Response create(final jAcl j) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vAcl acl = new vAcl(j);
    	
    	return Response.ok().entity(acl.read(requesteurGuid)).build();
    }
    
    
}
