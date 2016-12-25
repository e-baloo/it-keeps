package org.ebaloo.itkeeps.restapp.api;

import java.util.List;

import javax.ws.rs.*;
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
import org.ebaloo.itkeeps.core.domain.vertex.fCredential;

import com.codahale.metrics.annotation.Timed;

@Path("/")
public class rCredential {

	@Context
	SecurityContext securityContext;

	@GET // LIST
	@Produces({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_GET_ALL)
	public Response readAll() {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		List<jBaseLight> list = fCredential.readAll(requesterRid);
		return Response.ok().entity(list).build();
	}
	

	@GET // LIST
	@Produces({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_GET_ID + "{rid}")
	public Response readId(@PathParam(value = "rid") Rid rid) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		return Response.ok().entity(fCredential.read(requesterRid, rid)).build();
	}

	@POST // CREATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_CREATE)
	public Response create(final jCredential j) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		jCredential cred = fCredential.create(requesterRid, j);
		return Response.ok().entity(cred).build();
	}


	@POST // CREATE
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_CREATE_ID + "{rid}")
	public Response createId(final jCredential j, @PathParam(value = "rid") Rid rid) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		jCredential cred = fCredential.create(requesterRid, rid, j);
		return Response.ok().entity(cred).build();
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.USER)
	@Timed
	@Path(ApiPath.API_CRED_DELETE_ID + "{rid}")
	public Response delete(@PathParam(value = "rid") Rid rid) {
		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
		jCredential cred = fCredential.read(requesterRid, rid);
		fCredential.delete(requesterRid, rid);
		return Response.ok().entity(cred).build();
	}


}
