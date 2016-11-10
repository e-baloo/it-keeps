package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.pojo.JImage;
import org.ebaloo.itkeeps.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.domain.vertex.Image;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/image")
public class ImageEndpoint {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Timed
    public Response get() {

		List<JImage> list = new ArrayList<JImage>();
		
		for(BaseAbstract ba : Image.getAllBase(ModelFactory.get(Image.class), false)) {
		    	list.add(new JImage((Image) ba, false));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("/{id}")
    @Timed
    public Response getImage(@PathParam("id") String id) {
    	return Response.ok().entity(new JImage(Image.getImage(id))).build();
    }
	
    
    
    
    
}
