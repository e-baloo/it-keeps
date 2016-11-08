package org.ebaloo.itkeeps.restapp.test;


import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;
import org.ebaloo.itkeeps.restapp.authentication.AuthorizationRequestFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfig {
	
	public static final String PATH = "test/";
	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(TestEndpoint.class);
    	
    }
	
}