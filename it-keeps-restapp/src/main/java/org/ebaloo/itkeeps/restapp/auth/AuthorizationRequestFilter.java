package org.ebaloo.itkeeps.restapp.auth;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
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
import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;
import org.ebaloo.itkeeps.restapp.exception.ExceptionResponse;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;


@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    public static final Timer TIMER_FILTER = MetricsFactory.getMetricRegistry().timer(AuthorizationRequestFilter.class.getName() + ".filter");
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class.getName());
	private static final String BEARER = "Bearer";
    @Inject
	private
	javax.inject.Provider<UriInfo> uriInfo;
	@Context
	private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) {

		final Timer.Context timerContext = TIMER_FILTER.time();

		try {


			String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();
			Method method = resourceInfo.getResourceMethod();

			if (logger.isTraceEnabled())
				logger.trace("method : " + requestContext.getMethod() + " / path : " + path);

			if(requestContext.getMethod().toUpperCase().equals("OPTIONS")) {
				requestContext.abortWith( Response.status(Response.Status.NO_CONTENT).build());
				return;
			}

			if (logger.isTraceEnabled())
				logger.trace("getHeaders() : " + requestContext.getHeaders());


			// Access allowed for all
			if (method.isAnnotationPresent(PermitAll.class)) {
				if (logger.isTraceEnabled())
					logger.trace(path + "@PermitAll -> PASS");

                requestContext.setSecurityContext(new SecurityContextAuthorize(uriInfo, null, null));
                return;
			}
	
			// Get the HTTP Authorization header from the request
			String token = StringUtils.EMPTY;

			if(!StringUtils.isEmpty(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))) {
				String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

				// Check if the HTTP Authorization header is present and formatted
				// correctly
				if (!StringUtils.startsWithIgnoreCase(authorizationHeader, BEARER)) {

					if (logger.isTraceEnabled())
						logger.trace("AuthorizationHeader don't contain " + BEARER + " -> DENY");

					requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
					return;
				}

				// Extract the token from the HTTP Authorization header
				token = StringUtils.removeIgnoreCase(authorizationHeader, BEARER).trim();

			} else if (!StringUtils.isEmpty(requestContext.getHeaderString("x-access-token"))) {
				token = requestContext.getHeaderString("x-access-token").trim();
			} else {
				requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
				return;
			}
	
			final Map<String, Object> claims;
			try {
	
				claims = JwtFactory.isValid(token);
				
	
			} catch (Exception e) {
	
				if (logger.isTraceEnabled())
                    logger.trace("'token' is invalid -> DENY");

                requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
				return;
			}
			
			enRole requesterRole = JwtFactory.getRole(claims);
			String rid = JwtFactory.getRid(claims);
			
			
	        //Verify user access by Roles
	        if(method.isAnnotationPresent(aApplicationRolesAllowed.class))
	        {
	        	aApplicationRolesAllowed applicationRolesAllowed = method.getAnnotation(aApplicationRolesAllowed.class);
	            enRole applicationRole = applicationRolesAllowed.value() ;
	              
	            if(logger.isTraceEnabled())
	            	logger.trace(String.format("requesterRole : %s / applicationRole: %s = %s", requesterRole, applicationRole, requesterRole.isInRole(applicationRole)));

	            if(!requesterRole.isInRole(applicationRole)) {
	            
	    			if (logger.isTraceEnabled())
                        logger.trace("'token.role' is invalid -> DENY");

                    requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
	    			return;
	            }
	            	
	        }
			
			if (logger.isTraceEnabled())
                logger.trace("'token.role' is valid -> PASS");

            requestContext.setSecurityContext(new SecurityContextAuthorize(uriInfo, rid, requesterRole));
        } finally {
			timerContext.stop();
		}
	}
}
