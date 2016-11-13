package org.ebaloo.itkeeps.core.restapp.api;


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

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JUser;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.core.domain.vertex.Image;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.ebaloo.itkeeps.core.tools.SecurityRole;

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
			
			JUser juser = new JUser();
			((User) ba).apiFill(juser);
			
	    	list.add(juser);
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @Timed
    public Response getUser(@PathParam("id") String id) {

		JUser juser = new JUser();
		
		User user;
		
		if(Guid.isGuid(id)) {
			user = (User) User.getBaseAbstract(new Guid(id));
		} else {
			user = User.getById(id);
		}
		
		if(user == null)
			throw new RuntimeException("TODO"); // TODO
		
		
		user.apiFill(juser);
		
    	return Response.ok().entity(juser).build();
    }
	

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    //@Path("")
    @RolesAllowed({SecurityRole.ROOT, SecurityRole.ADMIN})
    @PermitAll
    @Timed
    public Response updateUser(final JUser juser) {
    	
    	System.out.println("X 1");
    	
    	User user = (User) User.getBaseAbstract(new Guid(juser.getGuid()));

    	System.out.println("X 2");

    	user.apiUpdate(juser);

    	System.out.println("X 3");

    	JUser newjuser = new JUser();

    	System.out.println("X 4");

    	user.apiFill(newjuser);

    	System.out.println("X 5");

    	return Response.ok().entity(newjuser).build();
    }

    
    
    
}
