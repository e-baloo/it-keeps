package org.ebaloo.itkeeps.tools;

import org.ebaloo.itkeeps.domain.pojo.JCredential;
import org.ebaloo.itkeeps.domain.vertex.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecurityFactory {

	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	
	public static void validateCredential(User user, JCredential credential) {
		
    	if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");

		
		if(!user.getId().equals(credential.getId()))
			throw new RuntimeException("TODO"); //TODO
		

		
		
		
		// TODO
	}
	

	public static void validateCredential(JCredential credential) {

		if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");
		
		
		// TODO
	}
	
	
	public enum AuthenticationType {
		
		BASIC,
		TOKEN,
		LDAP,
		ACTIVE_DIRECTORY
		
	}
	
	
	
	
}
