package org.ebaloo.itkeeps.core.restapp.api;

import org.ebaloo.itkeeps.core.restapp.InterfaceApplicationConfig;


public class ApiConfig {

	public static final String PATH = "api";

	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(AuthenticationTypeEndpoint.class);
    	
    	
    	
    	parent.classesAdd(ImageEndpoint.class);

    	
    	
    	parent.classesAdd(UserEndpoint.class);
    	parent.classesAdd(GroupEndpoint.class);

    	
    }

	
}
