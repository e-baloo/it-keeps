package org.ebaloo.itkeeps.restapp;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
 
/**
 * Jackson JSON processor could be controlled via providing a custom Jackson ObjectMapper instance. 
 * This could be handy if you need to redefine the default Jackson behavior and to fine-tune how 
 * your JSON data structures look like (copied from Jersey web site). * 
 * @see https://jersey.java.net/documentation/latest/media.html#d0e4799
 */
 
@Provider
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
//@Singleton
public class JacksonJsonProvider implements ContextResolver<ObjectMapper> {
    
	private static final Logger logger = LoggerFactory.getLogger(JacksonJsonProvider.class.getName());

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    static {
      MAPPER.setSerializationInclusion(Include.NON_EMPTY);
      MAPPER.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
      MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);      
      //MAPPER.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }
 
    public JacksonJsonProvider() {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("new()");
    	
    }
     
    @Override
    public ObjectMapper getContext(Class<?> type) {
    	
    	if(logger.isTraceEnabled())
    		logger.trace("getContext()");
    	
        return MAPPER;
    } 
}