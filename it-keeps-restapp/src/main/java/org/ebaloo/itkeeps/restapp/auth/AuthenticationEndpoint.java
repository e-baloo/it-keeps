package org.ebaloo.itkeeps.restapp.auth;


import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jToken;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.ebaloo.itkeeps.core.domain.vertex.vUser;
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
    public Response authenticateUser(jCredential credentials) {


    	System.out.println(credentials.getId() + " / " + credentials.getPassword());
    	
    	if(logger.isTraceEnabled())
    		logger.trace("authenticateUser()");
    	
        try {

            // Authenticate the user using the credentials provided
            vUser user = authenticate(credentials);

            
            // TODO
            
            
            String token = JwtFactory.getJwtString(user);
            
            return Response.ok(new jToken(token)).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
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