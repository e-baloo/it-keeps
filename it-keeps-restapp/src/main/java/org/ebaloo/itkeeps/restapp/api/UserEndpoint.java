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
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.ebaloo.itkeeps.core.domain.vertex.vUser;

import com.codahale.metrics.annotation.Timed;


@Path("")
public class UserEndpoint {

    @Context
    SecurityContext securityContext;

	
	@SuppressWarnings("unused")
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enSecurityRole.ADMIN)
    @Timed
    @Path(ApiPath.API_USER_GET_ALL)
    public Response readAll() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<jUser> list = new ArrayList<jUser>();
		
		for(vUser ba : vUser.getAllBase(null, vUser.class, false)) {
			jUser juser = new jUser();
			((vUser) ba).read(juser, new Guid(securityContext.getUserPrincipal().getName()));
	    	list.add(juser);
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_USER_GET_ID + "{id}")
	@aApplicationRolesAllowed(enSecurityRole.USER)
    @Timed
    public Response readId(@PathParam("id") String id) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		vUser user = vUser.get(null, vUser.class, id, false);
		
		if(user == null)
			throw new RuntimeException("readId(" + id + ") is null" );
		
		
    	return Response.ok().entity(user.read(new jUser(), requesteurGuid)).build();
    }
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_CRED_GET_ID + "{id}")
	@aApplicationRolesAllowed(enSecurityRole.USER)
    @Timed
    public Response readCredId(@PathParam("id") String id) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vCredential cred = vCredential.get(null, vCredential.class, id, false);
		
		if(cred == null)
			throw new RuntimeException("readId(" + id + ") is null" );
		
		
    	return Response.ok().entity(cred.getUser().read(new jUser(), requesteurGuid)).build();
    }

    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enSecurityRole.USER)
    @Timed
    @Path(ApiPath.API_USER_UPDATE)
    public Response update(final jUser juser) {
    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	vUser user = vUser.get(null, vUser.class,  juser.getGuid(), false);

    	user.update(juser, requesteurGuid);

    	jUser newjuser = new jUser();
    	vUser.get(null, vUser.class, juser.getGuid(), false).read(newjuser, requesteurGuid);

    	return Response.ok().entity(newjuser).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enSecurityRole.ADMIN)
    @Timed
    @Path(ApiPath.API_USER_CREATE)
    public Response create(final jUser juser) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	jUser user = vUser.create(juser, requesteurGuid);

    	return Response.ok().entity(user).build();
    }
    
    
}
