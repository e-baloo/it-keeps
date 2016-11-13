package org.ebaloo.itkeeps.core.restapp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.ebaloo.itkeeps.core.restapp.api.ApiConfig;
import org.ebaloo.itkeeps.core.restapp.authentication.AuthenticationConfig;
import org.ebaloo.itkeeps.core.restapp.exception.ApplicationExceptionMapper;
import org.ebaloo.itkeeps.core.restapp.image.ImageConfig;
import org.ebaloo.itkeeps.core.restapp.test.TestConfig;
import org.ebaloo.itkeeps.core.restapp.tools.ToolsConfig;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;
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

	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public void classesAdd(Class<?> c) {
		classes.add(c);
	}

	public ApplicationConfig() {

		if (logger.isTraceEnabled())
			logger.trace("ApplicationResourceConfig()");

		initializeApplication();
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
		ToolsConfig.init(this);
		AuthenticationConfig.init(this);
		TestConfig.init(this);
		ImageConfig.init(this);
		ApiConfig.init(this);
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