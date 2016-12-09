package org.ebaloo.itkeeps.restapp.api;


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
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.domain.vertex.fEntry;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rEntry {

    @Context
    SecurityContext securityContext;

	
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ENTRY_GET_ALL)
    public Response readAll() {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jBaseLight> list = fEntry.readAll(requesteurRid);
    	return Response.ok().entity(list).build();
	}
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_ENTRY_GET_ID + "{id}")
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    public Response readId(@PathParam() Rid rid) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jEntry entry = fEntry.read(requesteurRid, rid);
    	return Response.ok().entity(entry).build();
    }

    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_ENTRY_ENC_GET + "{id}")
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    public Response readEncId(@PathParam() Rid rid) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jEncryptedEntry encEntry = fEntry.readEncrypted(requesteurRid, rid);
    	return Response.ok().entity(encEntry).build();
    }

    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_ENTRY_UPDATE)
    public Response update(final jEntry j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	fEntry.update(requesteurRid, j);
    	return Response.ok().entity(fEntry.read(requesteurRid, j.getRid())).build();
    }

    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_ENTRY_ENC_UPDATE)
    public Response updateEnc(@PathParam() Rid rid, final jEncryptedEntry j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	fEntry.updateEncrypted(requesteurRid, rid, j);
    	return Response.ok().entity(fEntry.readEncrypted(requesteurRid, rid)).build();
    }
    
    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ENTRY_CREATE)
    public Response create(final jEntry j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jEntry entry = fEntry.create(requesteurRid, j);
    	return Response.ok().entity(fEntry.read(requesteurRid, entry.getRid())).build();
    }
    
    
}
