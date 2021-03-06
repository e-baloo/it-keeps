package org.ebaloo.itkeeps.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.typesafe.config.Config; 


public final class ConfigFactory {

	private static Logger mainLogger = null;

	
	public static Logger getManiLogger() {
		
		if(mainLogger == null) {

			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();

			mainLogger = LoggerFactory.getLogger("Main");
			
		}
		
		
		return mainLogger;
	}
	
	
 
	
    
    private static Config singleton = null;
    
    
    private static synchronized Config getConfig() {

        if (singleton == null) {
            singleton = getConfigFactory();
        }
    	
    	return singleton;
    }
    
    private static Config getConfigFactory() {
    	return com.typesafe.config.ConfigFactory.load();
    }
    
    private static boolean init = false;
    
    public static void init() {
    	
    	if(init)
    		return;
    	
		



		
		init = true;

    }

    
    public static boolean hasPath(String path) {
    	return getConfig().hasPath(path);
    }
    
	public static String getString(String path) {
		return getConfig().getString(path);
	}
	
	public static String getString(String path, String defaultValue) {
		
		if(hasPath(path)) {
			return getString(path);
		}
		
		return defaultValue;
		
	}

	private static int getInt(String path) {
		return getConfig().getInt(path);
	}

	public static int getInt(String path, int defaultValue) {

		if(hasPath(path)) {
			return getInt(path);
		}
		
		return defaultValue;
	}

	
	
	public static String getManifestInfoVersion(Class<?> clasz) {
		
		try {
		if(clasz == null)
			return "?";
		
		return clasz.getPackage().getImplementationVersion();
		} catch(Exception e) {
			return "?*";
		}
	}
}
