package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
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
import org.ebaloo.itkeeps.api.model.jImage;
import org.ebaloo.itkeeps.core.domain.vertex.vImage;

import com.codahale.metrics.annotation.Timed;


@Path("/")
public class ImageEndpoint {

    @Context
    SecurityContext securityContext;

	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    @Path(ApiPath.API_IMAGE_GET_ALL)
    public Response readAll() {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	
    	
		List<jImage> list = new ArrayList<jImage>();
		
		for(vImage image : vImage.getAllBase(null, vImage.class, false)) {
		    	list.add(image.read(requesteurGuid, false));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.API_IMAGE_GET_ID + "{id}")
    @aApplicationRolesAllowed(enRole.ADMIN)
    @Timed
    public Response getImage(@PathParam("id") String id) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	vImage image = vImage.getImage(id);
    	
    	return Response.ok().entity(image.read(requesteurGuid, true)).build();
    }
	
    
    
    
    
}
