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
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.domain.vertex.vGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class GroupEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(GroupEndpoint.class.getName());

    @Context
    SecurityContext securityContext;

	
	@GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_GET_ALL)
    public Response readAll() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<jGroup> list = new ArrayList<jGroup>();
		
		for(vGroup group : vGroup.getAllBase(null, vGroup.class, false)) {
	    	list.add(group.read(requesteurGuid));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
    @Timed
    @Path(ApiPath.API_GROUP_GET_ID + "{guid}")
    public Response read(@PathParam("guid") Guid guid) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		vGroup group = vGroup.get(null, vGroup.class, guid, false);
		
		if(group == null)
			throw new RuntimeException("TODO"); // TODO
		
    	return Response.ok().entity(group.read(requesteurGuid)).build();
    }
	

    @PUT 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_UPDATE)
    public Response update(final jGroup j) {
    	
		if (logger.isTraceEnabled())
			logger.trace("updateGroup()");

    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	vGroup group = vGroup.get(null, vGroup.class, j.getGuid(), false);

    	jGroup nj = group.update(j, requesteurGuid);

    	return Response.ok().entity(nj).build();
    }

    @POST 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_GROUP_CREATE)
    public Response create(final jGroup j) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vGroup group = new vGroup(j);

    	return Response.ok().entity(group.read(requesteurGuid)).build();
    }
    
    
}
