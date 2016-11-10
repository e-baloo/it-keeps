package org.ebaloo.itkeeps.restapp.authentication;


import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.domain.pojo.JCredentials;
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
    public Response authenticateUser(JCredentials credentials) {


    	System.out.println(credentials.getUsername() + " / " + credentials.getPassword());
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticateUser()");
    	
        try {

            // Authenticate the user using the credentials provided
            authenticate(credentials);

            // Issue a token for the user
            String token = issueToken(credentials);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(JCredentials credentials) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	// TODO
    	
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    }

    private String issueToken(JCredentials credentials) {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("issueToken()");

    	// TODO
    	
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	
    	return JwtFactory.getJwtString(credentials.getUsername(), credentials.getUsername(), null);
    	
    }
}