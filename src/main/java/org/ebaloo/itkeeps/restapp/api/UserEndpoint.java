package org.ebaloo.itkeeps.restapp.api;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.domain.Guid;
import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.pojo.JCredential;
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
    @Path("/{id}")
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @Timed
    public Response getUser(@PathParam("id") String id) {
    	return Response.ok().entity(new JUser((User) User.getBaseAbstract(new Guid(id)))).build();
    }
	

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    //@Path("")
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @Timed
    public Response updateUser(JUser juser) {
    	
    	System.out.println(juser.getGuid() + " / " + juser.getName());
    	
    	User user = (User) User.getBaseAbstract(new Guid(juser.getGuid()));
    	
    	System.out.println(user.getObjectVersion());
    	
    	juser.update(user);

    	System.out.println(user.getObjectVersion());

    	return Response.ok().entity(new JUser((User) user)).build();
    }

    
    
    
}
