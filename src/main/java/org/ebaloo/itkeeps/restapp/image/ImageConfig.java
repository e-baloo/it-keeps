package org.ebaloo.itkeeps.restapp.image;

import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class ImageConfig {

	public static final String PATH_IMAGE = "image";

	public static final String PATH_IMAGE_OBJECT = "imageObj";

	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(ImageEndpoint.class);
    	parent.classesAdd(ImageObjEndpoint.class);
        
    }

	
}
