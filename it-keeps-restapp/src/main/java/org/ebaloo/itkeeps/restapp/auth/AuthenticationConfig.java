package org.ebaloo.itkeeps.restapp.auth;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class AuthenticationConfig {
	
	public static final String PATH = "auth"; //"authentication/";
	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(AuthorizationRequestFilter.class);
    	parent.classesAdd(rAuthentication.class);
        
    }

}