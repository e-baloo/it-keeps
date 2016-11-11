package org.ebaloo.itkeeps.tools;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	
	public static class SecurityRole {

		public static final String GUEST = "GUEST";
		public static final String USER = "USER";
		public static final String ADMIN = "ADMIN";
		public static final String ROOT = "ROOT";

		private static final ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[]{GUEST, USER, ADMIN, ROOT}));
		
		public final static String[] values() {
			return list.toArray(new String[list.size()]);
		}
		
		public final static String valueOf(String arg0) {
			if(!list.contains(arg0)) 
				throw new RuntimeException("TODO"); //TODO
			
			return arg0;
		}
		
	}
	
	
	
	
}
