package org.ebaloo.itkeeps.restapp.authentication;

import java.lang.reflect.Method;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.restapp.exception.ExceptionResponse;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class.getName());

	
	private static final String BEARE = "Bearer";
	
	
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
			return;
		}

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if (!StringUtils.startsWithIgnoreCase(authorizationHeader,BEARE)) {
			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());
			return;
		}

		// Extract the token from the HTTP Authorization header
		String token = StringUtils.removeIgnoreCase(authorizationHeader, BEARE).trim();

		try {

			JwtFactory.isValid(token);

		} catch (Exception e) {

			requestContext.abortWith((new ExceptionResponse(Response.Status.UNAUTHORIZED)).getResponse());

		}
	}


}

/*
 * @Secured
 * 
 * @Provider
 * 
 * @Priority(Priorities.AUTHENTICATION) public class AuthenticationFilter
 * implements ContainerRequestFilter {
 * 
 * @Override public void filter(ContainerRequestContext requestContext) throws
 * IOException {
 * 
 * // Get the HTTP Authorization header from the request String
 * authorizationHeader =
 * requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
 * 
 * // Check if the HTTP Authorization header is present and formatted correctly
 * if (authorizationHeader == null ||
 * !authorizationHeader.startsWith("Bearer ")) { throw new
 * NotAuthorizedException("Authorization header must be provided"); }
 * 
 * // Extract the token from the HTTP Authorization header String token =
 * authorizationHeader.substring("Bearer".length()).trim();
 * 
 * try {
 * 
 * // Validate the token validateToken(token);
 * 
 * } catch (Exception e) { requestContext.abortWith(
 * Response.status(Response.Status.UNAUTHORIZED).build()); } }
 * 
 * private void validateToken(String token) throws Exception { // Check if it
 * was issued by the server and if it's not expired // Throw an Exception if the
 * token is invalid } }
 */