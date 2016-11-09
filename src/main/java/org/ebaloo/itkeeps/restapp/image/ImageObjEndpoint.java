package org.ebaloo.itkeeps.restapp.image;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.domain.vertex.Image;

import com.codahale.metrics.annotation.Timed;


@Path(ImageConfig.PATH_IMAGE_OBJECT)
public class ImageObjEndpoint {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("/{id}")
    @Timed
    public Response getImage(@PathParam("id") String id) {
    	
        Image img = Image.getImage(id);

    	jImage ji = new jImage();
    	ji.guid = img.getGuid().toString();
    	ji.name = img.getName();
    	ji.imageType = img.getImageType();
    	ji.type = img.getType();
    	ji.base64 = img.getBase64();
        
    	return Response.ok().entity(ji).build();
    	
    }
    
    
    
    public static class jImage {
    	
    	public String type;
    	public String guid;
    	public String name;
    	public String imageType;
    	public String base64;
    	
    	
    }
	
}
