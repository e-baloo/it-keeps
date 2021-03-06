package org.ebaloo.itkeeps.restapp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.ebaloo.itkeeps.core.tools.MetricsFactory;
import org.ebaloo.itkeeps.restapp.exception.ApplicationExceptionMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.jersey2.MetricsFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@ApplicationPath("/*")
public class ApplicationConfig extends ResourceConfig implements InterfaceApplicationConfig {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class.getName());

	private Set<Class<?>> classes = new HashSet<>();

	public ApplicationConfig() {

		if (logger.isTraceEnabled())
			logger.trace("ApplicationResourceConfig()");

		initializeApplication();
	}

    public void classesAdd(Class<?> c) {
        classes.add(c);
    }

	private void initializeApplication() {

		setApplicationName("IT-Keeps");

		registerFeatures(); // Register features
		registerProviders(); // Register providers
		registerListeners(); // Register listeners
		registerResources(); // Register resources

		
		registerClasses(classes);
	}

	private void registerFeatures() {
		register(JacksonFeature.class); // Enable Jackson parsing support
		register(ObjectMapperContextResolver.class);
		
		register(SseFeature.class); // Enable Server sent events
	}

	private void registerListeners() {
		register(new MetricsFeature(MetricsFactory.getMetricRegistry()));
	}

	private void registerResources() {
        org.ebaloo.itkeeps.restapp.tools.Index.init(this);
        org.ebaloo.itkeeps.restapp.auth.Index.init(this);
        org.ebaloo.itkeeps.restapp.img.Index.init(this);
        org.ebaloo.itkeeps.restapp.api.Index.init(this);
    }

	private void registerProviders() {
		classes.add(ApplicationExceptionMapper.class);
	}

	@Override
	public ResourceConfig getResourceConfig() {
		return this;
	}

	
	@Provider
	public static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	    final ObjectMapper mapper = new ObjectMapper();

	    public ObjectMapperContextResolver() {
	    	
	    	
	        mapper.registerModule(new JodaModule());
	        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
		    	    WRITE_DATES_AS_TIMESTAMPS , false);
	    }

	    @Override
	    public ObjectMapper getContext(Class<?> type) {
	        return mapper;
	    }  
	}
	
}