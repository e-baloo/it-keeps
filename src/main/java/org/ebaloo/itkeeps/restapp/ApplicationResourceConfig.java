package org.ebaloo.itkeeps.restapp;

import org.ebaloo.itkeeps.restapp.authentication.AuthenticationResourceConfig;
import org.ebaloo.itkeeps.restapp.test.TestResourceConfig;
import org.ebaloo.itkeeps.restapp.tools.ToolsResourceConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationResourceConfig extends ResourceConfig  /*Application*/ {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationResourceConfig.class.getName());

	
    public ApplicationResourceConfig() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("ApplicationResourceConfig()");

    	
    	register(org.glassfish.jersey.server.filter.UriConnegFilter.class);
    	register(org.glassfish.jersey.jackson.JacksonFeature.class);
    	register(ExceptionMapper.class);
    	register(JacksonJsonProvider.class);
    	
    	
        register(ToolsResourceConfig.class);
        register(AuthenticationResourceConfig.class);
        register(TestResourceConfig.class);
        
        
        register(org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainerProvider.class);
    }
	
}