package org.ebaloo.itkeeps.core.tools;

import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.vertex.vBase;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.ebaloo.itkeeps.core.domain.vertex.vUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecurityFactory {

	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	
	public static void validateCredential(vUser user, jCredential jcredential) {
		
    	if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");

		
    	vCredential credential = vCredential.get(null, vCredential.class, jcredential.getId(), false);
    	
		if(!user.equals(credential.getUser()))
			throw new RuntimeException("TODO"); //TODO
		
		if(credential.getAuthenticationType().equals(enAuthentication.BASIC)) {
			;
		} else {
			throw new RuntimeException("TODO"); //TODO
		}
		
		
		// TODO
	}
	

	public static final void validateCredential(jCredential credential) {

		if(logger.isTraceEnabled())
    		logger.trace("validateCredential()");
		
		checkRootUserExist(credential);
		
		// TODO
	}
	
	
	
	private static final void checkRootUserExist(jCredential jcredential) {

		if(logger.isTraceEnabled())
    		logger.trace("checkRootUserExist()");

		// TODO Add CONF "Create Root User"
		try {
		String sql = "SELECT COUNT(*) FROM " + vUser.class.getSimpleName() + " WHERE " + vBase.WhereClause.ENABLE_IS_TRUE + " AND (" +jUser.ROLE + "='" + enSecurityRole.ROOT.toString() +"')" ;   
		
		
		Long rootUserCount = GraphFactory.command(null,  sql).get(0).getProperty("COUNT");
		logger.trace("rootUserCount : " + rootUserCount.toString());
		
		if(rootUserCount == 0) {
			vCredential cred = new vCredential(jcredential, null);
			cred.commit();
			vUser user = cred.getUser();
			user.commit();
			user.setRole(enSecurityRole.ROOT);
			
			logger.trace("checkRootUserExist() cred :" + cred.toString());
			logger.trace("checkRootUserExist() user :" + user.toString());
			
		}}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
}
