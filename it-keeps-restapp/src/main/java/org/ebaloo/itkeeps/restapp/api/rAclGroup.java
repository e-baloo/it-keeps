package org.ebaloo.itkeeps.restapp.api;


import java.util.List;

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
import org.ebaloo.itkeeps.api.model.jAclGroup;
import org.ebaloo.itkeeps.core.domain.vertex.fAclGroup;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rAclGroup {

    @Context
    SecurityContext securityContext;

	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ROOT)
    @Timed
    @Path(ApiPath.API_ACL_GRP_GET_ALL)
    public Response readAll() {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jAclGroup> list = fAclGroup.readAll(requesteurRid);
    	return Response.ok().entity(list).build();
	}
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ROOT)
    @Timed
    @Path(ApiPath.API_ACL_GRP_GET_ID + "{rid}")
    public Response readId(@PathParam(value = "rid") Rid rid) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        jAclGroup j = fAclGroup.read(requesteurRid, rid);
        return Response.ok().entity(j).build();
    }
	
    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ROOT)
    @Timed
    @Path(ApiPath.API_ACL_GRP_UPDATE)
    public Response update(jAclGroup j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	j = fAclGroup.update(requesteurRid, j);
    	return Response.ok().entity(j).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ROOT)
    @Timed
    @Path(ApiPath.API_ACL_GRP_CREATE)
    public Response create(jAclGroup j) {

    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());

    	j = fAclGroup.create(requesteurRid, j);
    	
    	return Response.ok().entity(j).build();
    }
    
    @DELETE //DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ROOT)
    @Timed
    @Path(ApiPath.API_ACL_GRP_DELETE + "{rid}")
    public Response delete(@PathParam(value = "rid") Rid rid) {
        Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
        jAclGroup j = fAclGroup.delete(requesteurRid, rid);
    	return Response.ok().entity(j).build();
    }
    

    
}
