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
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.domain.vertex.fGroup;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class rGroup {

    @Context
    SecurityContext securityContext;

	
	@GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_GET_ALL)
    public Response readAll() {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	List<jBaseLight> list = fGroup.readAll(requesteurRid);
    	return Response.ok().entity(list).build();
	}
	
	
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_GROUP_GET_ID + "{rid}")
    public Response read(@PathParam() Rid rid) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jGroup j = fGroup.read(requesteurRid, rid);
    	return Response.ok().entity(j).build();
    }
	

    @PUT 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_UPDATE)
    public Response update(final jGroup j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	fGroup.update(requesteurRid, j);
    	return Response.ok().entity(fGroup.read(requesteurRid, j.getRid())).build();
    }

    
    @POST 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_CREATE)
    public Response create(final jGroup j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jGroup group = fGroup.create(requesteurRid, j);
    	return Response.ok().entity(fGroup.read(requesteurRid, group.getRid())).build();
    }
    
    
}
