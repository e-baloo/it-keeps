package org.ebaloo.itkeeps;

import org.ebaloo.itkeeps.restapp.authentication.JwtFactory;
import org.ebaloo.itkeeps.tools.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.typesafe.config.Config; 


public final class ConfigFactory {

	private static Logger mainLogger = null;

	public static final String CONF_HTTP = "http";
	public static final String CONF_HTTP_PORT = CONF_HTTP + "." + "port";

	public static final String CONF_LOG = "log";
	public static final String CONF_LOG_LEVEL = "level";

	public static final String CONF_TOKEN = "token";
	public static final String CONF_TOKEN_TIMEOUT = "timeout";
	public static final String CONF_TOKEN_PASSWORD = "password";

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
    	
    	if(getConfig() != null)
    		return;


		// LOGGER
		if(getConfig().hasPath(CONF_LOG)) {
			
			Config logConf = getConfig().getConfig(CONF_LOG);
			
			if(logConf.hasPath(CONF_LOG_LEVEL))
				initLog(logConf.getString(CONF_LOG_LEVEL)); 
			
		}

		

		// TOKEN
		if(getConfig().hasPath(CONF_TOKEN)) {
			
			Config tokenConf = getConfig().getConfig(CONF_LOG);
			
			if(tokenConf.hasPath(CONF_TOKEN_TIMEOUT))
				JwtFactory.setTimeout(tokenConf.getInt(CONF_TOKEN_TIMEOUT)); 

			if(tokenConf.hasPath(CONF_TOKEN_PASSWORD))
				JwtFactory.setPassword(tokenConf.getString(CONF_TOKEN_PASSWORD)); 

		}
		

		// STATS
		if(getConfig().hasPath(CONF_STATS)) {
			
			Config statsConf = getConfig().getConfig(CONF_STATS);
			
			if(statsConf.hasPath(CONF_STATS_JVM))
				if(statsConf.getBoolean(CONF_STATS_JVM))
					MetricsFactory.enableJvm();
					
		}

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