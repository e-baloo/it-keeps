package org.ebaloo.itkeeps;

import java.net.URI;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.ebaloo.itkeeps.database.DatabaseFactory;
import org.ebaloo.itkeeps.domain.Base;
import org.ebaloo.itkeeps.domain.BaseStandard;
import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.restapp.ApplicationConfig;
import org.glassfish.grizzly.http.server.HttpServer;


/**
 * 
 * @author Marc Donval
 *
 */
public class App {


	
	
	
	private static String baseUriPort = "8080";
	
    private static URI baseUri = null;

    public static void main(String[] args) {
        
    	try {
    		
    		ConfigFactory.init();
    		
    		DatabaseFactory.init();
    		
    		ModelFactory.init();
    		
    		
    		Base b = new BaseStandard("Marc");
    		
    		ConfigFactory.getManiLogger().warn(b.getGuid().toString());
    		ConfigFactory.getManiLogger().warn(BaseStandard.findByGuidOrName(ModelFactory.get(BaseStandard.class), "Marc").getGuid().toString());
    		
    		
    		baseUriPort = ConfigFactory.getString(ConfigFactory.CONF_HTTP_PORT, baseUriPort);
    		
    		
    		baseUri = URI.create("http://localhost:" + baseUriPort + "/");
    		ConfigFactory.getManiLogger().trace(baseUri.toString());
        
    		ConfigFactory.getManiLogger().info("Application Starting");
        	
    		
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, new ApplicationConfig());
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            
            
    		ConfigFactory.getManiLogger().info("Application Started!");

            Thread.currentThread().join();
        } catch (InterruptedException ex) {
        	ConfigFactory.getManiLogger().error(null, ex);
        }
    }
    
    
    
    


    
}