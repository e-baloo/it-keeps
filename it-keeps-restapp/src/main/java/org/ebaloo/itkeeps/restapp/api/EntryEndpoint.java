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
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.domain.vertex.vEntry;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class EntryEndpoint {

    @Context
    SecurityContext securityContext;

	
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ENTRY_GET_ALL)
    public Response readAll() {
		
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());

		List<jEntry> list = new ArrayList<jEntry>();
		
		for(vEntry entry : vEntry.getAllBase(null, vEntry.class, false)) {
	    	list.add(entry.read(requesteurRid));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_ENTRY_GET_ID + "{id}")
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    public Response readId(@PathParam("id") String id) {

    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());

    	vEntry entry = vEntry.get(null, vEntry.class, id, false);
		
		if(entry == null)
			throw new RuntimeException("readId(" + id + ") is null" );
		
		
    	return Response.ok().entity(entry.read(requesteurRid)).build();
    }
	
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_ENTRY_UPDATE)
    public Response update(final jEntry juser) {
    	
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	
    	vEntry entry = vEntry.get(null, vEntry.class,  juser.getRid(), false);

    	entry.update(juser, requesteurRid);

    	return Response.ok().entity(entry.read(requesteurRid)).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ENTRY_CREATE)
    public Response create(final jEntry juser) {

    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());

    	vEntry entry = new vEntry(juser);

    	return Response.ok().entity(entry.read(requesteurRid)).build();
    }
    
    
}
