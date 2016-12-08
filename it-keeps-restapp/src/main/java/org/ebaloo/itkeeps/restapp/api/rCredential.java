package org.ebaloo.itkeeps.restapp.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.domain.vertex.fCredential;
import org.ebaloo.itkeeps.core.domain.vertex.fUser;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;

import com.codahale.metrics.annotation.Timed;

@Path("/")
public class rCredential {

	@Context
	SecurityContext securityContext;

	@GET // LIST
	@Produces({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.ADMIN)
	@Timed
	@Path(ApiPath.API_CRED_GET_ALL)
	public Response readAll() {
		Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jBaseLight> list = fCredential.readAll(requesteurRid);
		return Response.ok().entity(list).build();
	}
	
	@GET // READ
	@Produces({ MediaType.APPLICATION_JSON })
	@Path(ApiPath.API_CRED_GET_ID + "{id}")
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	public Response readCredId(@PathParam("id") String id) {

		Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());

		vCredential cred = vCredential.get(null, vCredential.class, id, false);

		if (cred == null)
			throw new RuntimeException("readId(" + id + ") is null");

		return Response.ok().entity(fUser.read(requesteurRid, cred.getUser().getRid())).build();
	}


	@POST // CREATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_CREATE)
	public Response create(final jCredential j) {
		Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		jCredential cred = fCredential.create(requesteurRid, j);
		return Response.ok().entity(cred).build();
	}


	@POST // CREATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_CREATE_ID + "{id}")
	public Response createId(final jCredential j, @PathParam("id") String id) {

		Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
		Rid userRid = new Rid(id);

		jCredential cred = fCredential.create(requesteurRid, userRid, j);
		
		return Response.ok().entity(cred).build();
	}


	
}
