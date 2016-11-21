package org.ebaloo.itkeeps.restapp.auth;


import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.api.model.JToken;
import org.ebaloo.itkeeps.core.domain.vertex.Credential;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.ebaloo.itkeeps.core.tools.SecurityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





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
            User user = authenticate(credentials);

            
            // TODO
            
            
            String token = issueToken(user);
            
            return Response.ok(new JToken(token)).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }



    
    private User authenticate(JCredential jcredential) throws Exception {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticate()");
    	
    	SecurityFactory.validateCredential(jcredential);
    	
    	Credential credential = Credential.get(null, Credential.class, jcredential.getId(), false);
    	if(credential == null) 
    		throw new RuntimeException("credential is null!");
    	
    	User user = credential.getUser();
    	
    	if(user == null) 
    		throw new RuntimeException("user is null!");

    	
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