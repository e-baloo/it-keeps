package org.ebaloo.itkeeps.restapp.test;


import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;

public class TestConfig {
	
	public static final String PATH = "test/";
	
    public static void init(InterfaceApplicationConfig parent) {
    	
    	parent.classesAdd(TestEndpoint.class);
    	
    }
	
}