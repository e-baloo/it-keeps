package org.ebaloo.itkeeps.restapp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.ebaloo.itkeeps.restapp.api.ApiConfig;
import org.ebaloo.itkeeps.restapp.authentication.AuthenticationConfig;
import org.ebaloo.itkeeps.restapp.exception.ApplicationExceptionMapper;
import org.ebaloo.itkeeps.restapp.image.ImageConfig;
import org.ebaloo.itkeeps.restapp.test.TestConfig;
import org.ebaloo.itkeeps.restapp.tools.ToolsConfig;
import org.ebaloo.itkeeps.tools.MetricsFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.jersey2.MetricsFeature;

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

}