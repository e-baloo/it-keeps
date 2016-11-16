package org.ebaloo.itkeeps.restapp.tools;


import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class ToolsConfig {
	
	public static final String PATH = "tools/";
	
	public static void init(InterfaceApplicationConfig parent) {
		
		parent.classesAdd(PingEndpoint.class);
		parent.classesAdd(StatsEndpoint.class);
		
	}
	
}