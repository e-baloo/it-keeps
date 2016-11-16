package org.ebaloo.itkeeps.core.tools;

import org.ebaloo.itkeeps.api.annotation.ApplicationRolesAllowed.SecurityRole;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.api.model.JUser;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.vertex.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecurityFactory {

	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	
	public static void validateCredential(User user, JCredential credential) {
		
    	if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");

		
		if(!user.getUserId().equals(credential.getId()))
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
	

	public static final void validateCredential(JCredential credential) {

		if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");
		
		checkRootUserExist(credential);
		
		// TODO
	}
	
	
	
	private static final void checkRootUserExist(JCredential credential) {
		
		// TODO Add CONF "Create Root User"
		try {
		String sql = "SELECT COUNT(*) FROM " + User.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND (" +JUser.ROLE + "='" + SecurityRole.ROOT.toString() +"')" ;   
		if(((Long) GraphFactory.command(null,  sql).get(0).getProperty("COUNT")) == 0) {
			(new User(credential)).setRole(SecurityRole.ROOT);
		}}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
}
