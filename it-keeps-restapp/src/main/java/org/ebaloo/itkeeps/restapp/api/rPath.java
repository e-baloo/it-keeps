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
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.core.domain.vertex.fPath;

import com.codahale.metrics.annotation.Timed;


@Path("")
public class rPath {

    @Context
    SecurityContext securityContext;

	
	@GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_PATH_GET_ALL)
    public Response read() {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jPath> list = fPath.readAll(requesteurRid);
		return Response.ok().entity(list).build();
	}
	
	
	
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_PATH_GET_ID + "{rid}")
    public Response read(@PathParam("rid") Rid RID) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		jPath path = fPath.read(requesteurRid, RID);
    	return Response.ok().entity(path).build();
    }
	

    @PUT 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_PATH_UPDATE)
    public Response update(final jPath j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	fPath.update(requesteurRid, j);
    	return Response.ok().entity(fPath.read(requesteurRid, j.getRid())).build();
    }

    
    @POST 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_PATH_CREATE)
    public Response create(final jPath j) {
    	Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    	jPath path = fPath.create(requesteurRid, j);
    	return Response.ok().entity(fPath.read(requesteurRid, path.getRid())).build();
    }
    
    
}
