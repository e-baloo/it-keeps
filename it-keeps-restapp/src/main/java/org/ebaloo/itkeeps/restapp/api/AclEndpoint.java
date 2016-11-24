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

	
	@SuppressWarnings("unused")
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_GET_ALL)
    public Response readAll() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<jAcl> list = new ArrayList<jAcl>();
		
		for(vAcl entry : vAcl.getAllBase(null, vAcl.class, false)) {
			jAcl juser = new jAcl();
			entry.read(juser, new Guid(securityContext.getUserPrincipal().getName()));
	    	list.add(juser);
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

    	vAcl entry = vAcl.get(null, vAcl.class, id, false);
		
		if(entry == null)
			throw new RuntimeException("readId(" + id + ") is null" );
		
		
    	return Response.ok().entity(entry.read(new jAcl(), requesteurGuid)).build();
    }
	
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_UPDATE)
    public Response update(final jAcl juser) {
    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	vAcl user = vAcl.get(null, vAcl.class,  juser.getGuid(), false);

    	user.update(juser, requesteurGuid);

    	jAcl newjuser = new jAcl();
    	vAcl.get(null, vAcl.class, juser.getGuid(), false).read(newjuser, requesteurGuid);

    	return Response.ok().entity(newjuser).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_CREATE)
    public Response create(final jAcl jacl) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vAcl acl = new vAcl(jacl);
    	
    	return Response.ok().entity(acl.read(null, requesteurGuid)).build();
    }
    
    
}
