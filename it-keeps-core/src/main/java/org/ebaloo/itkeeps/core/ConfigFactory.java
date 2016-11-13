package org.ebaloo.itkeeps.core;

import org.ebaloo.itkeeps.core.restapp.authentication.JwtFactory;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.typesafe.config.Config; 


public final class ConfigFactory {

	private static Logger mainLogger = null;

	public static final String CONF_HTTP = "http";
	public static final String CONF_HTTP_PORT = CONF_HTTP + "." + "port";

	public static final String CONF_LOG = "log";
	public static final String CONF_LOG_LEVEL = CONF_LOG + "." + "level";


	public static final String CONF_STATS = "stats";
	public static final String CONF_STATS_JVM = "jvm";
	
	
	
	public static final Logger getManiLogger() {
		
		if(mainLogger == null) {

			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();

			mainLogger = LoggerFactory.getLogger("Main");
		}
		
		
		return mainLogger;
	}
	
	
    private static final void initLog(final String logLevel) {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, logLevel);
		
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		mainLogger = LoggerFactory.getLogger(App.class.getName());
		
		mainLogger.info("Start log @" + logLevel);
    }
	
    
    private static Config singleton = null;
    
    
    public static synchronized final Config getConfig() {

        if (singleton == null) {
            singleton = getConfigFactory();
        }
    	
    	return singleton;
    }
    
    private static final Config getConfigFactory() {
    	return com.typesafe.config.ConfigFactory.load();
    }
    
    private static boolean init = false;
    
    public static void init() {
    	
    	if(init)
    		return;
    	
		// LOGGER
    	if(getConfig().hasPath(CONF_LOG_LEVEL))
    		initLog(getConfig().getString(CONF_LOG_LEVEL)); 

		


		// STATS
		if(getConfig().hasPath(CONF_STATS)) {
			
			Config statsConf = getConfig().getConfig(CONF_STATS);
			
			if(statsConf.hasPath(CONF_STATS_JVM))
				if(statsConf.getBoolean(CONF_STATS_JVM))
					MetricsFactory.enableJvm();
					
		}
		
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

	public static int getInt(String path) {
		return getConfig().getInt(path);
	}

	public static int getInt(String path, int defaultValue) {

		if(hasPath(path)) {
			return getInt(path);
		}
		
		return defaultValue;
	}
    
	
}
