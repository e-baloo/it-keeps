package org.ebaloo.itkeeps;

import java.net.URI;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.ebaloo.itkeeps.database.DatabaseFactory;
import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.vertex.Base;
import org.ebaloo.itkeeps.domain.vertex.BaseStandard;
import org.ebaloo.itkeeps.domain.vertex.Image;
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
    		
    		
    		
    		Image i = new Image("icon:default", "image/png", "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");
    		
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