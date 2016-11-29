package org.ebaloo.itkeeps.restapp.tools;


import org.ebaloo.itkeeps.restapp.InterfaceApplicationConfig;


public class ToolsConfig {
	
	public static void init(InterfaceApplicationConfig parent) {
		
		parent.classesAdd(rTools.class);
		
	}
	
}