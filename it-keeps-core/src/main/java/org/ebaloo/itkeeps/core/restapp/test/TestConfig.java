package org.ebaloo.itkeeps.core.restapp.test;


import org.ebaloo.itkeeps.core.restapp.InterfaceApplicationConfig;

public class TestConfig {
	
	public static final String PATH = "test/";
	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(TestEndpoint.class);
    	
    }
	
}