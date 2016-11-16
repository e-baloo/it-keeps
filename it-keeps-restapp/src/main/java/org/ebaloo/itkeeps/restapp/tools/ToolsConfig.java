package org.ebaloo.itkeeps.core.restapp.tools;


import org.ebaloo.itkeeps.core.restapp.InterfaceApplicationConfig;


public class ToolsConfig {
	
	public static final String PATH = "tools/";
	
	public static void init(InterfaceApplicationConfig parent) {
		
		parent.classesAdd(PingEndpoint.class);
		parent.classesAdd(StatsEndpoint.class);
		
	}
	
}