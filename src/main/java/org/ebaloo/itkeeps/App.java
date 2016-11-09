package org.ebaloo.itkeeps;

import java.net.URI;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.ebaloo.itkeeps.restapp.ApplicationConfig;
import org.ebaloo.itkeeps.restapp.authentication.JwtFactory;
import org.ebaloo.itkeeps.tools.MetricsFactory;
import org.glassfish.grizzly.http.server.HttpServer;


/**
 * 
 * @author Marc Donval
 *
 */
public class App {

	private static Logger logger = null;

	private static final String CONF_HTTP = "http";
	private static final String CONF_HTTP_PORT = "port";

	private static final String CONF_LOG = "log";
	private static final String CONF_LOG_LEVEL = "level";

	private static final String CONF_TOKEN = "token";
	private static final String CONF_TOKEN_TIMEOUT = "timeout";
	private static final String CONF_TOKEN_PASSWORD = "password";

	private static final String CONF_STATS = "stats";
	private static final String CONF_STATS_JVM = "jvm";
	
	
	
	private static String baseUriPort = "8080";
	private static String logLevel = "INFO";
	
    private static URI baseUri = null;

    public static void main(String[] args) {
        
    	try {
    		
    		init();
    		
    		
    		
    		baseUri = URI.create("http://localhost:" + baseUriPort + "/");
    		logger.trace(baseUri.toString());
        
    		logger.info("Application Starting");
        	
    		/*
            Map<String, String> initParams = new HashMap<>();
            initParams.put(
                    ServerProperties.PROVIDER_PACKAGES,
                    ApplicationResourceConfig.class.getPackage().getName());
                    */
    		
    		
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, new ApplicationConfig());
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            
            
    		logger.info("Application Started!");

            Thread.currentThread().join();
        } catch (InterruptedException ex) {
        	logger.error(null, ex);
        }
    }
    
    
    
    
    private static Config conf = null;
    
    
    private static void init() {
    	
    	if(conf != null)
    		return;

		Config conf = ConfigFactory.load();

		// LOGGER
		if(conf.hasPath(CONF_LOG)) {
			
			Config logConf = conf.getConfig(CONF_LOG);
			
			if(logConf.hasPath(CONF_LOG_LEVEL))
				logLevel = logConf.getString(CONF_LOG_LEVEL); 
			
		}
		initLog();

		
		// HTTP
		if(conf.hasPath(CONF_HTTP)) {
			
			Config httpConf = conf.getConfig(CONF_HTTP);
			
			if(httpConf.hasPath(CONF_HTTP_PORT))
				baseUriPort = httpConf.getString(CONF_HTTP_PORT); 
			
		}
		

		// TOKEN
		if(conf.hasPath(CONF_TOKEN)) {
			
			Config tokenConf = conf.getConfig(CONF_LOG);
			
			if(tokenConf.hasPath(CONF_TOKEN_TIMEOUT))
				JwtFactory.setTimeout(tokenConf.getInt(CONF_TOKEN_TIMEOUT)); 

			if(tokenConf.hasPath(CONF_TOKEN_PASSWORD))
				JwtFactory.setPassword(tokenConf.getString(CONF_TOKEN_PASSWORD)); 

		}
		

		// STATS
		if(conf.hasPath(CONF_STATS)) {
			
			Config statsConf = conf.getConfig(CONF_STATS);
			
			if(statsConf.hasPath(CONF_STATS_JVM))
				if(statsConf.getBoolean(CONF_STATS_JVM))
					MetricsFactory.enableJvm();
					
		}

    }
    
    private static final void initLog() {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, logLevel);
		
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		logger = LoggerFactory.getLogger(App.class.getName());
    }
    
}