package org.ebaloo.itkeeps.restapp.api;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class ApiConfig {

	public static final String PATH = "api";

	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(rEnum.class);
    	
    	parent.classesAdd(ImageEndpoint.class);

    	
    	
    	parent.classesAdd(rUser.class);
    	parent.classesAdd(rCredential.class);
    	parent.classesAdd(rGroup.class);
    	parent.classesAdd(rPath.class);
    	parent.classesAdd(rEntry.class);
    	parent.classesAdd(rAcl.class);
    	parent.classesAdd(rAclGroup.class);

    	
    }

	
}
