package org.ebaloo.itkeeps.restapp.auth;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;
import org.ebaloo.itkeeps.restapp.exception.ExceptionResponse;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class.getName());

	
	private static final String BEARE = "Bearer";
	
    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

	@Context
	private ResourceInfo resourceInfo;

	
	
	@Override
	public void filter(ContainerRequestContext requestContext) {

		if (logger.isTraceEnabled())
			logger.trace("filter()");

		String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();

		Method method = resourceInfo.getResourceMethod();
		// Access allowed for all
		if (method.isAnnotationPresent(PermitAll.class)) {
			if (logger.isTraceEnabled())
				logger.trace(path + "->  @PermitAll");
			
            requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, null, null));
			return;
		}

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if (!StringUtils.startsWithIgnoreCase(authorizationHeader, BEARE)) {
			
			if (logger.isTraceEnabled())
				logger.trace("AuthorizationHeader dont containe " + BEARE + " -> Deny");
			
			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
			return;
		}

		// Extract the token from the HTTP Authorization header
		String token = StringUtils.removeIgnoreCase(authorizationHeader, BEARE).trim();

		final Map<String, Object> claims;
		try {

			claims = JwtFactory.isValid(token);
			

		} catch (Exception e) {

			if (logger.isTraceEnabled())
				logger.trace("'token' is invalide -> Deniy");

			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
			return;
		}
		
		enRole role = JwtFactory.getRole(claims);
		String guid = JwtFactory.getGuid(claims);
		
		
        //Verify user access by Roles
        if(method.isAnnotationPresent(aApplicationRolesAllowed.class))
        {
        	aApplicationRolesAllowed applicationRolesAllowed = method.getAnnotation(aApplicationRolesAllowed.class);
            enRole tRole = applicationRolesAllowed.value() ;
              
            logger.info("role : " + role + "  /  tRole: " + tRole);
            if(tRole.isInRole(role)) {
            
    			if (logger.isTraceEnabled())
    				logger.trace("'token.role' is invalide -> Deniy");
            	
    			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
    			return;
            }
            	
        }
		
		if (logger.isTraceEnabled())
			logger.trace("'token.role' is valide -> Ok");
		
		requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, guid, role));
		
	}


}
