package org.ebaloo.itkeeps.core.restapp.api;


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
import org.ebaloo.itkeeps.api.model.JUser;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.core.domain.vertex.Image;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed.SecurityRole;

import com.codahale.metrics.annotation.Timed;


@Path(ApiConfig.PATH + "/user")
public class UserEndpoint {

    @Context
    SecurityContext securityContext;

	
	@SuppressWarnings("unused")
	@GET // LIST
    @Produces({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response read() {
		
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		List<JUser> list = new ArrayList<JUser>();
		
		for(BaseAbstract ba : Image.getAllBase(null, ModelFactory.get(User.class), false)) {
			JUser juser = new JUser();
			((User) ba).apiFill(juser, new Guid(securityContext.getUserPrincipal().getName()));
	    	list.add(juser);
		}
		
    	return Response.ok().entity(list).build();
	}
	
	
	
    @GET //READ
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
	@ApplicationRolesAllowed(SecurityRole.USER)
    @Timed
    public Response readId(@PathParam("id") String id) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

		JUser juser = new JUser();
		User user;
		
		if(Guid.isGuid(id)) {
			user = User.getBaseAbstract(null, ModelFactory.get(User.class), new Guid(id));
		} else {
			user = User.getById(null, id);
		}
		
		if(user == null)
			throw new RuntimeException("TODO"); // TODO
		
		user.apiFill(juser, requesteurGuid);
		
    	return Response.ok().entity(juser).build();
    }
	

    @PUT // UPDATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.USER)
    @Timed
    public Response update(final JUser juser) {
    	
    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());
    	
    	User user = User.getBaseAbstract(null, ModelFactory.get(User.class),  juser.getGuid());

    	user.apiUpdate(juser, requesteurGuid);

    	JUser newjuser = new JUser();
    	User.getBaseAbstract(null, ModelFactory.get(User.class), juser.getGuid()).apiFill(newjuser, requesteurGuid);

    	return Response.ok().entity(newjuser).build();
    }

    @POST // CREATE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@ApplicationRolesAllowed(SecurityRole.ADMIN)
    @Timed
    public Response create(final JUser juser) {

    	Guid requesteurGuid = new Guid(securityContext.getUserPrincipal().getName());

    	User user = new User(juser);
    	JUser newjuser = new JUser();
    	user.apiFill(newjuser, requesteurGuid);

    	return Response.ok().entity(newjuser).build();
    }
    
    
}
