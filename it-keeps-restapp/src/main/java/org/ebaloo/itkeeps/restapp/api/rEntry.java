package org.ebaloo.itkeeps.restapp.api;


import java.util.List;

import javax.ws.rs.*;
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


    @Timed
    @GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_ENTRY_GET_ALL)
    public Response readAll() {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jBaseLight> list = fEntry.readAll(requesteurRid);
    	return Response.ok().entity(list).build();
	}


    @Timed
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ENTRY_GET_ID + "{rid}")
    public Response readId(@PathParam(value = "rid") Rid rid) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        jEntry entry = fEntry.read(requesteurRid, rid);
    	return Response.ok().entity(entry).build();
    }

    @Timed
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ENTRY_ENC_GET + "{rid}")
    public Response readEncId(@PathParam(value = "rid") Rid rid) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        jEncryptedEntry encEntry = fEntry.readEncrypted(requesteurRid, rid);
        return Response.ok().entity(encEntry).build();
    }

    @Timed
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ENTRY_UPDATE)
    public Response update(final jEntry j) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        fEntry.update(requesteurRid, j);
        return Response.ok().entity(fEntry.read(requesteurRid, j.getRid())).build();
    }

    @Timed
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ENTRY_ENC_UPDATE + "{rid}")
    public Response updateEnc(@PathParam(value = "rid") Rid rid, final jEncryptedEntry j) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        fEntry.updateEncrypted(requesteurRid, rid, j);
        return Response.ok().entity(fEntry.readEncrypted(requesteurRid, rid)).build();
    }

    @Timed
    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_ENTRY_CREATE)
    public Response create(final jEntry j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jEntry entry = fEntry.create(requesteurRid, j);
    	return Response.ok().entity(fEntry.read(requesteurRid, entry.getRid())).build();
    }


    @Timed
    @DELETE // DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_ENTRY_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        jEntry j = fEntry.delete(requesteurRid, rid);
        return Response.ok().entity(j).build();
    }
}
