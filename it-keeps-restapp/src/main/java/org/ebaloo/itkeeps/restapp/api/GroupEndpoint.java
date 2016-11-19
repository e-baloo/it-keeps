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

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.annotation.ApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.ApplicationRolesAllowed.SecurityRole;
import org.ebaloo.itkeeps.api.model.JGroup;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.core.domain.vertex.Image;
import org.ebaloo.itkeeps.core.domain.vertex.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/group")
public class GroupEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(GroupEndpoint.class.getName());

    @Context
    SecurityContext securityContext;

	
	@GET 
    @Produces({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response read() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<JGroup> jl = new ArrayList<JGroup>();
		
		for(BaseAbstract ba : Image.getAllBase(null, ModelFactory.get(Group.class), false)) {
			JGroup j = new JGroup();
			((Group) ba).read(j, requesteurGuid);
	    	jl.add(j);
		}
		
    	return Response.ok().entity(jl).build();
	}
	
	
	
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{guid}")
	@ApplicationRolesAllowed(SecurityRole.USER)
    @Timed
    public Response read(@PathParam("guid") Guid guid) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		Group group = Group.get(null, ModelFactory.get(Group.class), guid, false);
		
		if(group == null)
			throw new RuntimeException("TODO"); // TODO
		
		JGroup j = group.read(null, requesteurGuid);
		
    	return Response.ok().entity(j).build();
    }
	

    @PUT 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response update(final JGroup j) {
    	
		if (logger.isTraceEnabled())
			logger.trace("updateGroup()");

    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	Group group = Group.get(null, ModelFactory.get(Group.class), j.getGuid(), false);

    	JGroup nj = group.update(j, requesteurGuid);

    	return Response.ok().entity(nj).build();
    }

    @POST 
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response create(final JGroup j) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	Group group = new Group(j);
    	JGroup nj = new JGroup();
    	group.read(nj, requesteurGuid);

    	return Response.ok().entity(nj).build();
    }
    
    
}
