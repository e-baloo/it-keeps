package org.ebaloo.itkeeps.restapp.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.domain.vertex.fUser;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;

import com.codahale.metrics.annotation.Timed;

@Path("/")
public class rUser {

	@Context
	SecurityContext securityContext;

	@POST // CREATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.ADMIN)
	@Timed
	@Path(ApiPath.API_USER_CREATE)
	public Response create(final jUser juser) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		jUser user = fUser.create(requesterRid, juser);
		return Response.ok().entity(user).build();
	}

	@DELETE // DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.ADMIN)
	@Timed
	@Path(ApiPath.API_USER_DELETE + "{rid}")
	public Response delete(@PathParam(value = "rid") Rid rid) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		jUser j = fUser.delete(requesterRid, rid);
		return Response.ok().entity(j).build();
	}

	@GET // LIST
	@Produces({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.ADMIN)
	@Timed
	@Path(ApiPath.API_USER_GET_ALL)
	public Response readAll() {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jBaseLight> list = fUser.readAll(requesterRid);
		return Response.ok().entity(list).build();
	}

	


	@GET // READ
	@Produces({ MediaType.APPLICATION_JSON })
	@Path(ApiPath.API_USER_GET_ID + "{rid}")
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	public Response readId(@PathParam(value = "rid") Rid rid) {

		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());

		jUser user = fUser.read(requesterRid, rid);

		return Response.ok().entity(user).build();
	}


	@PUT // UPDATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_USER_UPDATE)
	public Response update(final jUser j) {

		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());

		jUser user = fUser.update(requesterRid, j);

		return Response.ok().entity(user).build();
	}

	
	@GET // READ
	@Produces({ MediaType.APPLICATION_JSON })
	@Path(ApiPath.API_USER_GET_CRED_ID + "{id}")
	@aApplicationRolesAllowed(enRole.GUEST)
	@Timed
	public Response readCredId(@PathParam(value = "id") String id) {

		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());

		vCredential cred = vCredential.get(null, vCredential.class, id, false);

		if (cred == null)
			throw new RuntimeException("readId(" + id + ") is null");

		return Response.ok().entity(fUser.read(requesterRid, cred.getUser().getRid())).build();
	}
}
