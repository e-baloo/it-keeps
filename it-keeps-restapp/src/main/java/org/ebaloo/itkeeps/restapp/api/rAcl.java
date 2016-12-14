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
import org.ebaloo.itkeeps.api.enumeration.*;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.domain.vertex.fAcl;

import com.codahale.metrics.annotation.Timed;

import java.util.stream.Collectors;


@Path("/")
public class rAcl {

    @Context
    SecurityContext securityContext;


    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ACL_DATA_ENUM)
    public Response getAclDataEnum() {
        return Response.ok().entity(enAclData.values().stream().map(enAbstract::name).collect(Collectors.toList())).build();
    }

    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ACL_OWNER_ENUM)
    public Response getAclOwnerEnum() {
        return Response.ok().entity(enAclOwner.values().stream().map(enAbstract::name).collect(Collectors.toList())).build();
    }

    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ACL_ADMIN_ENUM)
    public Response getAclAdminEnum() {
        return Response.ok().entity(enAclAdmin.values().stream().map(enAbstract::name).collect(Collectors.toList())).build();
    }


    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.USER)
    @Path(ApiPath.API_ACL_ROLE_ENUM)
    public Response getAclRoleEnum() {
        return Response.ok().entity(enAclRole.values().stream().map(enAbstract::name).collect(Collectors.toList())).build();
    }




    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_GET_ID + "{rid}")
    public Response readId(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jAcl acl = fAcl.read(requesterRid, rid);
        return Response.ok().entity(acl).build();
    }
	
    
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_UPDATE)
    public Response update(final jAcl j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        fAcl.update(requesterRid, j);
        return Response.ok().entity(fAcl.read(requesterRid, j.getId())).build();
    }

    
    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_CREATE)
    public Response create(final jAcl j) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jAcl acl = fAcl.create(requesterRid, j);
        return Response.ok().entity(fAcl.read(requesterRid, acl.getId())).build();
    }

    
    @DELETE //DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_ACL_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
        jAcl j = fAcl.delete(requesterRid, rid);
        return Response.ok().entity(j).build();
    }
}
