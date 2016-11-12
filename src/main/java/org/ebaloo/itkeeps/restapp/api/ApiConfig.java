package org.ebaloo.itkeeps.restapp.api;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class ApiConfig {

	public static final String PATH = "api";

	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(AuthenticationTypeEndpoint.class);
    	
    	
    	
    	parent.classesAdd(ImageEndpoint.class);

    	
    	
    	parent.classesAdd(UserEndpoint.class);

    	
    }

	
}
