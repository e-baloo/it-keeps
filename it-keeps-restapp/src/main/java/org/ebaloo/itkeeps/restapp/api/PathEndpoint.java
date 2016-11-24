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
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.core.domain.vertex.vPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;


@Path("")
public class PathEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(PathEndpoint.class.getName());

    @Context
    SecurityContext securityContext;

	
	@GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_PATH_GET_ALL)
    public Response read() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<jPath> jl = new ArrayList<jPath>();
		
		for(vPath ba : vPath.getAllBase(null, vPath.class, false)) {
			jPath j = new jPath();
			((vPath) ba).read(j, requesteurGuid);
	    	jl.add(j);
		}
		
    	return Response.ok().entity(jl).build();
	}
	
	
	
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_PATH_GET_ID + "{guid}")
    public Response read(@PathParam("guid") Guid guid) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		vPath group = vPath.get(null, vPath.class, guid, false);
		
		if(group == null)
			throw new RuntimeException("TODO"); // TODO
		
		jPath j = group.read(null, requesteurGuid);
		
    	return Response.ok().entity(j).build();
    }
	

    @PUT 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_PATH_UPDATE)
    public Response update(final jPath j) {
    	
		if (logger.isTraceEnabled())
			logger.trace("updateGroup()");

    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	vPath group = vPath.get(null, vPath.class, j.getGuid(), false);

    	jPath nj = group.update(j, requesteurGuid);

    	return Response.ok().entity(nj).build();
    }

    @POST 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_PATH_CREATE)
    public Response create(final jPath j) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vPath group = new vPath(j);
    	jPath nj = new jPath();
    	group.read(nj, requesteurGuid);

    	return Response.ok().entity(nj).build();
    }
    
    
}
