package org.ebaloo.itkeeps.restapp.authentication;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

			logger.trace("token = " + token);
			
			claims = JwtFactory.isValid(token);
			

		} catch (Exception e) {

			if (logger.isTraceEnabled())
				logger.trace("'token' is invalide -> Deniy");

			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
			return;
		}
		
		List<String> roles = JwtFactory.getRoles(claims);
		String guid = JwtFactory.getGuid(claims);
		
		
        //Verify user access by Roles
        if(method.isAnnotationPresent(RolesAllowed.class))
        {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
              
            if(CollectionUtils.intersection(roles, rolesSet).size() <= 0) {
            	
    			if (logger.isTraceEnabled())
    				logger.trace("'token.roles' is invalide -> Deniy");
            	
    			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
    			return;
            }
            	
        }
		
		
		requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, guid, roles));
		
	}


}
