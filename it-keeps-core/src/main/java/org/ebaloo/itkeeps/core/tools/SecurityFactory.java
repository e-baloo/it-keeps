package org.ebaloo.itkeeps.core.tools;

import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecurityFactory {

	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	
	public static void validateCredential(User user, JCredential credential) {
		
    	if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");

		
		if(!user.getId().equals(credential.getId()))
			throw new RuntimeException("TODO"); //TODO
		

		switch(credential.getAuthenticationType()) {
		
		case BASIC:
			
			break;
		case LDAP:
		case TOKEN:
		case ACTIVE_DIRECTORY:
		default:
			throw new RuntimeException("TODO"); //TODO
		
		}
		
		
		// TODO
	}
	

	public static void validateCredential(JCredential credential) {

		if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");
		
		
		// TODO
	}
	
	
	
	
}
