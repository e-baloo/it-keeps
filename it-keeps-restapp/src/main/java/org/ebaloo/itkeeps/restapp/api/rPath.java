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
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.domain.vertex.fPath;

import com.codahale.metrics.annotation.Timed;
import org.ebaloo.itkeeps.core.domain.vertex.fUser;


@Path("")
public class rPath {

    @Context
    SecurityContext securityContext;


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_PATH_GET_ALL)
    public Response read() {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        List<jBaseLight> list = fPath.readAll(requesterRid);
        return Response.ok().entity(list).build();
	}


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_PATH_GET_ID + "{rid}")
    public Response read(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jPath path = fPath.read(requesterRid, rid);
        return Response.ok().entity(path).build();
    }


    @Timed
    @DELETE // DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_PATH_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jPath j = fPath.delete(requesterRid, rid);
        return Response.ok().entity(j).build();
    }


    @Timed
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_PATH_UPDATE)
    public Response update(final jPath j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        fPath.update(requesterRid, j);
        return Response.ok().entity(fPath.read(requesterRid, j.getRid())).build();
    }


    @Timed
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_PATH_CREATE)
    public Response create(final jPath j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jPath path = fPath.create(requesterRid, j);
        return Response.ok().entity(fPath.read(requesterRid, path.getRid())).build();
    }
    
    
}
