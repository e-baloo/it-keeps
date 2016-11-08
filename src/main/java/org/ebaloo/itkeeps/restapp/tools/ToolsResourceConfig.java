package org.ebaloo.itkeeps.restapp.tools;


import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ToolsResourceConfig extends ResourceConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ToolsResourceConfig.class.getName());

	public static final String PATH = "tools/";
	
    public ToolsResourceConfig() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("new()");
    	
        register(PingEndpoint.class);
    }
	
}