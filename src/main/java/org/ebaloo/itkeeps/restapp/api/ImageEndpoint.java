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
import org.ebaloo.itkeeps.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.domain.vertex.Image;
import org.ebaloo.itkeeps.restapp.api.pojo.JImage;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/image")
public class ImageEndpoint {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Timed
    public Response get() {

		List<JImage> list = new ArrayList();
		
		for(BaseAbstract ba : Image.getAllBase(ModelFactory.get(Image.class), false, false)) {
			
		       Image image = (Image) ba;

		    	JImage ji = new JImage();
		    	
		    	ji.setGuid(image.getGuid().toString());
		    	ji.setName(image.getName());
		    	ji.setImageType(image.getImageType());
		    	ji.setType(image.getType());
		
		    	list.add(ji);
		}
		
		
    	return Response.ok().entity(list).build();

	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("/{id}")
    @Timed
    public Response getImage(@PathParam("id") String id) {
    	
        Image img = Image.getImage(id);

    	JImage ji = new JImage();
    	
    	ji.setGuid(img.getGuid().toString());
    	ji.setName(img.getName());
    	ji.setImageType(img.getImageType());
    	ji.setType(img.getType());
    	//ji.setBase64(img.getBase64());
        
    	return Response.ok().entity(ji).build();
    	
    }
	
}
