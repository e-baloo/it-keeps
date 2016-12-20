package org.ebaloo.itkeeps.restapp.auth;


import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.api.enumeration.enAuthenticationType;
import org.ebaloo.itkeeps.api.model.*;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory;
import org.ebaloo.itkeeps.core.domain.vertex.fUser;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import java.util.stream.Collectors;


@Path("")
public class rAuthentication {

	private static final Logger logger = LoggerFactory.getLogger(rAuthentication.class.getName());
    @Context
    SecurityContext securityContext;

    @GET
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.AUTH_TYPE_ENUM)
    public Response getAuthTypeEnum() {
        return Response.ok().entity(enAuthenticationType.values().stream().map(enAbstract::name).collect(Collectors.toList())).build();
    }


    @POST
    @Timed
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.AUTH_LOGIN)
    public Response login(jCredential credentials) {
        try {
            jUser user = authenticate(credentials);
            String token = JwtFactory.getJwtString(user);
            return Response.ok(new jToken(token)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


	@PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.GUEST)
    @Path(ApiPath.AUTH_RENEW)
    @Timed
    public Response renew() {

        try {
        	
    		Rid requesterRid = new Rid(securityContext.getUserPrincipal().getName());
    		jUser user = fUser.read(requesterRid, requesterRid);
            String token = JwtFactory.getJwtString(user);
            return Response.ok(new jToken(token)).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @aApplicationRolesAllowed(enRole.GUEST)
    @Path(ApiPath.AUTH_LOGOUT)
    @Timed
    public Response logout() {
        // TODO
        return Response.ok().build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.AUTH_CHECK)
    @Timed
    public Response check(String txt) {
        try {
            jToken token = jToken.MAPPER.readValue(txt, jToken.class);
        	JwtFactory.isValid(token.getToken());
            return Response.status(Response.Status.OK).entity(Boolean.TRUE).build();
        } catch (Exception e) {
            return Response.status(Response.Status.OK).entity(Boolean.FALSE).build();
        }      
    }

    
    private jUser authenticate(jCredential jcredential) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	SecurityFactory.validateCredential(jcredential);

        vCredential credential = vCredential.get(null, vCredential.class, jcredential.getName(), false);
        if(credential == null)
    		throw new RuntimeException("credential is null!");
    	
    	jBaseLight user = credential.getUser();
    	
    	if(user == null) 
    		throw new RuntimeException("user is null!");


        return fUser.read(user.getId(), user.getId());
    }
}