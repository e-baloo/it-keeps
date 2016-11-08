package org.ebaloo.itkeeps.restapp.authentication;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthenticationResourceConfig extends ResourceConfig  /*Application*/ {
	
	
	public static final String PATH = "auth"; //"authentication/";
	
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationResourceConfig.class.getName());

	
    public AuthenticationResourceConfig() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("ApplicationResourceConfig()");

    	
        register(AuthorizationRequestFilter.class);
        register(AuthenticationEndpoint.class);
        
    }
	
}