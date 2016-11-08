package org.ebaloo.itkeeps.restapp.authentication;

import java.lang.reflect.Method;

import javax.annotation.security.PermitAll;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class.getName());

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
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION.toLowerCase());

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = extractJwtTokenFromAuthorizationHeader(authorizationHeader);

		try {

			JwtFactory.isValid(token, JwtFactory.key);

		} catch (Exception e) {
			
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			
		}
	}

    public static String extractJwtTokenFromAuthorizationHeader(String auth) {
        //Replacing "Bearer Token" to "Token" directly
        return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "").replace(" ", "");
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