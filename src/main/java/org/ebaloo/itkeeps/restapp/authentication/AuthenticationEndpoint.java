package org.ebaloo.itkeeps.restapp.authentication;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
	curl --data "username=marc&password=password" http://127.0.0.1:8080/auth/login

*/

@Path(AuthenticationResourceConfig.PATH)
public class AuthenticationEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationEndpoint.class.getName());

	
    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    @PermitAll
    @Path("login")
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password) {

    	if(logger.isTraceEnabled())
    		logger.trace("authenticateUser()");
    	
        try {

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	// TODO
    	
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    }

    private String issueToken(String username) {
    	return issueToken(username, null);
    }
    
    private String issueToken(String username, List<String> roles) {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("issueToken()");

    	// TODO
    	
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	
    	return JwtFactory.getJwtString(username, null, 0, JwtFactory.key);
    	
    }
}