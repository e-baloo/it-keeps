package org.ebaloo.itkeeps.restapp;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyApplicationEventListener implements ApplicationEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class.getName());
	
    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent.getType()) {
            case INITIALIZATION_START:
                logger.info("Initialization started");
                break;
            case INITIALIZATION_FINISHED:
                logger.info("Initialization finished");
                break;
            case INITIALIZATION_APP_FINISHED:
                logger.info("Initialization of APP finished");
                break;
            case RELOAD_FINISHED:
                logger.info("Reload completed");
                break;
            case DESTROY_FINISHED:
                logger.info("Destroy completed");
                break;
        }
    }
 
 
    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return new MyRequestEventListener();
    }


    public static class MyRequestEventListener implements RequestEventListener {
        private volatile long methodStartTime;

        @Override
        public void onEvent(RequestEvent requestEvent) {
        	
        	System.out.println("onEvent");
        	
            switch (requestEvent.getType()) {
                case RESOURCE_METHOD_START:
                    methodStartTime = System.currentTimeMillis();
                    break;
                case RESOURCE_METHOD_FINISHED:
                    long methodExecution = System.currentTimeMillis() - methodStartTime;
                    final String methodName = requestEvent.getUriInfo().getMatchedResourceMethod().getInvocable().getHandlingMethod().getName();
                    logger.info("Method '" + methodName + "' executed. Processing time: " + methodExecution + " ms");
                    break;
			case EXCEPTION_MAPPER_FOUND:
				break;
			case EXCEPTION_MAPPING_FINISHED:
				break;
			case FINISHED:
				break;
			case LOCATOR_MATCHED:
				break;
			case MATCHING_START:
				break;
			case ON_EXCEPTION:
				break;
			case REQUEST_FILTERED:
				break;
			case REQUEST_MATCHED:
				break;
			case RESP_FILTERS_FINISHED:
				break;
			case RESP_FILTERS_START:
				break;
			case START:
				break;
			case SUBRESOURCE_LOCATED:
				break;
			default:
				break;
            }
        }
    }
}