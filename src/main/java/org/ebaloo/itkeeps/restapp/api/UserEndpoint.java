package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.pojo.JImage;
import org.ebaloo.itkeeps.domain.pojo.JUser;
import org.ebaloo.itkeeps.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.domain.vertex.Image;
import org.ebaloo.itkeeps.domain.vertex.User;
import org.ebaloo.itkeeps.tools.SecurityRole;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/user")
public class UserEndpoint {

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @Timed
    public Response get() {

		List<JUser> list = new ArrayList<JUser>();
		
		for(BaseAbstract ba : Image.getAllBase(ModelFactory.get(User.class), false)) {
		    	list.add(new JUser((User) ba));
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("/{id}")
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @Timed
    public Response getImage(@PathParam("id") String id) {
    	return Response.ok().entity(new JImage(Image.getImage(id))).build();
    }
	
    
    
    
    
}
