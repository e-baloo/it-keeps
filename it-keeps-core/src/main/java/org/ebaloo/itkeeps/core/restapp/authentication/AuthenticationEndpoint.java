package org.ebaloo.itkeeps.core.restapp.authentication;


import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.api.model.JToken;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.ebaloo.itkeeps.core.tools.SecurityFactory;
import org.ebaloo.itkeeps.core.tools.SecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/*
	curl --data "username=marc&password=password" http://127.0.0.1:8080/auth/login

*/

@Path(AuthenticationConfig.PATH)
public class AuthenticationEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationEndpoint.class.getName());

	
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path("login")
    public Response authenticateUser(JCredential credentials) {


    	System.out.println(credentials.getId() + " / " + credentials.getPassword());
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticateUser()");
    	
        try {

            // Authenticate the user using the credentials provided
            this.user = authenticate(credentials);

            
            // TODO
            ArrayList<String> listRoles = new ArrayList<String>();
            listRoles.add(SecurityRole.USER);
            listRoles.add(SecurityRole.ADMIN);
            listRoles.add(SecurityRole.ROOT);
            this.user.setRoles(listRoles);
            
            
            String token = issueToken(this.user);
            
            return Response.ok(new JToken(token)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    @Context
    User user;

    
    private User authenticate(JCredential credentials) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	SecurityFactory.validateCredential(credentials);
    	
    	User user = User.getByCredentials(credentials);
    	
    	if(user == null) 
    		throw new RuntimeException("user '" + credentials.getId() + "' not definde!");

    	
    	return user;
    }

    

    
    private String issueToken(User user) {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("issueToken()");

    	// TODO
    	
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	
    	return JwtFactory.getJwtString(user);
    	
    }
}