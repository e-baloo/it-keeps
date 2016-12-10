package org.ebaloo.itkeeps.restapp.img;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class Index {

	public static final String PATH_IMAGE = "img";


	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(ImageEndpoint.class);
        
    }

	
}
