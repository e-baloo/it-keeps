package org.ebaloo.itkeeps.restapp.api;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jImage;
import org.ebaloo.itkeeps.core.domain.vertex.fImage;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rImage {

    @Context
    SecurityContext securityContext;


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_IMAGE_GET_ALL)
    public Response readAll() {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        return Response.ok().entity(fImage.readAll(requesterRid)).build();
    }


    @Timed
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path(ApiPath.API_IMAGE_GET_ID + "{id}")
    @aApplicationRolesAllowed(enRole.ADMIN)
    public Response getImage(@PathParam(value = "id") String id) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        return Response.ok().entity(fImage.read(requesterRid, id)).build();
    }


    @Timed
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_IMAGE_UPDATE)
    public Response updateEnc(final jImage j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        fImage.update(requesterRid, j);
        return Response.ok().entity(fImage.read(requesterRid, j.getRid().toString())).build();
    }


    @Timed
    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_IMAGE_CREATE)
    public Response create(final jImage j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jImage image = fImage.create(requesterRid, j);
        return Response.ok().entity(fImage.read(requesterRid, image.getRid().toString())).build();
    }


    @Timed
    @DELETE // DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Path(ApiPath.API_IMAGE_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jImage j = fImage.delete(requesterRid, rid);
        return Response.ok().entity(j).build();
    }


}
