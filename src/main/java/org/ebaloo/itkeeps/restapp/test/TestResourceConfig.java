package org.ebaloo.itkeeps.restapp.test;


import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestResourceConfig extends ResourceConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(TestResourceConfig.class.getName());

	public static final String PATH = "test/";
	
    public TestResourceConfig() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("new()");
    	
        register(TestEndpoint.class);
    }
	
}