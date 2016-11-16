package org.ebaloo.itkeeps.core.restapp.image;

import org.ebaloo.itkeeps.core.restapp.InterfaceApplicationConfig;


public class ImageConfig {

	public static final String PATH_IMAGE = "image";


	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(ImageEndpoint.class);
        
    }

	
}
