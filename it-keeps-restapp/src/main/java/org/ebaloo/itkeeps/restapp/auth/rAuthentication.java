package org.ebaloo.itkeeps.restapp.auth;


import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jToken;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.ebaloo.itkeeps.core.domain.vertex.vUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;





@Path("/")
public class rAuthentication {

	private static final Logger logger = LoggerFactory.getLogger(rAuthentication.class.getName());

	
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.AUTH_LOGIN)
    @Timed
    public Response login(jCredential credentials) {

        try {
            vUser user = authenticate(credentials);

            String token = JwtFactory.getJwtString(user);
            
            return Response.ok(new jToken(token)).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    
/*
    @Timed
*/
    
	@Context
	SecurityContext securityContext;

	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
	@aApplicationRolesAllowed(enRole.GUEST)
    @Path(ApiPath.AUTH_RENEW)
    @Timed
    public Response renew() {

        try {
        	
    		Rid requesteurRid = new Rid(securityContext.getUserPrincipal().getName());
    		vUser user = vUser.get(null, vUser.class, requesteurRid, false);
            String token = JwtFactory.getJwtString(user);
            return Response.ok(new jToken(token)).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

	
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.AUTH_CHECK)
    @Timed
    public Response check(jToken token) {
        try {
        	JwtFactory.isValid(token.getToken());
            return Response.status(Response.Status.OK).entity(Boolean.TRUE).build();
        } catch (Exception e) {
            return Response.status(Response.Status.OK).entity(Boolean.FALSE).build();
        }      
    }

    
    private vUser authenticate(jCredential jcredential) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	SecurityFactory.validateCredential(jcredential);
    	
    	vCredential credential = vCredential.get(null, vCredential.class, jcredential.getId(), false);
    	if(credential == null) 
    		throw new RuntimeException("credential is null!");
    	
    	vUser user = credential.getUser();
    	
    	if(user == null) 
    		throw new RuntimeException("user is null!");

    	
    	return user;
    }
}