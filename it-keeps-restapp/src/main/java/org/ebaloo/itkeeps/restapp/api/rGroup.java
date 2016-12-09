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
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.domain.vertex.fGroup;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rGroup {

    @Context
    SecurityContext securityContext;


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_GROUP_GET_ALL)
    public Response readAll() {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        List<jBaseLight> list = fGroup.readAll(requesterRid);
        return Response.ok().entity(list).build();
	}


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_GROUP_GET_ID + "{rid}")
    public Response read(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jGroup j = fGroup.read(requesterRid, rid);
        return Response.ok().entity(j).build();
    }

    @Timed
    @DELETE // DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_GROUP_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jGroup j = fGroup.delete(requesterRid, rid);
        return Response.ok().entity(j).build();
    }


    @Timed
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_GROUP_UPDATE)
    public Response update(final jGroup j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        fGroup.update(requesterRid, j);
        return Response.ok().entity(fGroup.read(requesterRid, j.getRid())).build();
    }


    @Timed
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_GROUP_CREATE)
    public Response create(final jGroup j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jGroup group = fGroup.create(requesterRid, j);
        return Response.ok().entity(fGroup.read(requesterRid, group.getRid())).build();
    }
    
    
}
