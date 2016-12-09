package org.ebaloo.itkeeps.restapp.img;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.core.domain.vertex.vImage;

import com.codahale.metrics.annotation.Timed;


@Path("")
public class ImageEndpoint {

	
	private static vImage defaultIcon = null;
	
    @GET
    @Produces()
    @PermitAll
    @Path(ApiPath.IMAGE_GET_ID + "{id}")
    @Timed
    public Response getImage(@PathParam() String id) {
    	
        vImage img = vImage.getImage(id);

        if(img == null) {
            if(id.startsWith(vImage.ICON_NAME_PREFIX)) {
                if(defaultIcon == null) {
                    defaultIcon = vImage.getImage(vImage.DEFAULT_ICON);
                }
                img = defaultIcon;
            } else {
                throw new RuntimeException("TODO"); //TODO
            }
        }
    	
    	return Response.ok().entity(img.getImageBytes()).type(img.getImageType()).build();
    	
    }
	
}
