package org.ebaloo.itkeeps.restapp.auth;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class Index {

    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(AuthorizationRequestFilter.class);
    	parent.classesAdd(CORSFilter.class);
    	parent.classesAdd(rAuthentication.class);
        
    }

}