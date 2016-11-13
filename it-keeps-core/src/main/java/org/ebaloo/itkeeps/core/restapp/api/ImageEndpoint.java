package org.ebaloo.itkeeps.core.restapp.api;


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

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JImage;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.core.domain.vertex.Image;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed.SecurityRole;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/image")
public class ImageEndpoint {

    @Context
    SecurityContext securityContext;

	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response get() {

		List<JImage> list = new ArrayList<JImage>();
		
		for(BaseAbstract ba : Image.getAllBase(ModelFactory.get(Image.class), false)) {
			
			JImage jimage = new JImage();
			((Image) ba).apiFill(jimage, new Guid(securityContext.getUserPrincipal().getName()), false);
			
			
		    	list.add(jimage);
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("/{id}")
    @ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response getImage(@PathParam("id") String id) {
    	
		JImage jimage = new JImage();
		Image.getImage(id).apiFill(jimage, new Guid(securityContext.getUserPrincipal().getName()), false);
    	
    	return Response.ok().entity(jimage).build();
    }
	
    
    
    
    
}
