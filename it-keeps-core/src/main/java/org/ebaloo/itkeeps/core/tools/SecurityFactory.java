package org.ebaloo.itkeeps.core.tools;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;
import org.ebaloo.itkeeps.core.domain.vertex.vAcl;
import org.ebaloo.itkeeps.core.domain.vertex.vAclAdmin;
import org.ebaloo.itkeeps.core.domain.vertex.vAclData;
import org.ebaloo.itkeeps.core.domain.vertex.vAclOwner;
import org.ebaloo.itkeeps.core.domain.vertex.vBase;
import org.ebaloo.itkeeps.core.domain.vertex.vBaseChildAcl;
import org.ebaloo.itkeeps.core.domain.vertex.vCredential;
import org.ebaloo.itkeeps.core.domain.vertex.vUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;


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
	
	
	
	
	/*
	 * 
	 * ACL
	 * 
	 */
	
	public static class SecurityAcl {

		private enAclOwner aclOwner = enAclOwner.FALSE;
		private enAclData aclData = null;
		private Set<enAclAdmin> aclAdmin = new HashSet<enAclAdmin>();
		
		private static final String V_ACL_OWNER = vAclOwner.class.getSimpleName();
		
		
		
		private void put(OrientVertex ov) {
			
			String type = ov.getType().getName();
			
			if(V_ACL_OWNER.equals(type)) {
				putAclOwner(ov);
				return;
			}

			if(type.equals(vAclData.class.getSimpleName())) {
				putAclData(ov);
				return;
			}
			
			if(type.equals(vAclAdmin.class.getSimpleName())) {
				putAclAdmin(ov);
				return;
			}

			throw new RuntimeException("The type : " + type + " is unknow !");
		}
		
		private void putAclAdmin(OrientVertex ov) {
			this.aclAdmin.add(enAclAdmin.valueOf(ov.getProperty(vAclAdmin.NAME)));
		}

		private void putAclData(OrientVertex ov) {
			
			enAclData tAclData = enAclData.valueOf(ov.getProperty(vAclData.NAME));
			
			if(this.aclData == null ) {
				this.aclData = tAclData;
				return;
			}
			
			if(this.aclData.ordinal() < tAclData.ordinal()) {
				this.aclData = tAclData;
			}
		}

		private void putAclOwner(OrientVertex ov) {
			
			if(this.aclOwner.value() == Boolean.TRUE)
				return;
			
			this.aclOwner = enAclOwner.valueOf(ov.getProperty(vAclOwner.NAME));
		}
		
		
	}
	
	
	public static SecurityAcl getSecurityAcl(vBaseChildAcl src, vBaseChildAcl dst) {
		
		long time = System.currentTimeMillis();
		

		if(src == null) 
			throw new RuntimeException("TODO"); // TODO

		if(dst == null) 
			throw new RuntimeException("TODO"); // TODO

		
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT FROM ( ");
		sb.append("	 TRAVERSE OUT('" + eAclNoTraverse.class.getSimpleName() + "') FROM ( ");
		
		sb.append("    SELECT FROM (TRAVERSE OUT('" + eAclRelation.class.getSimpleName() + "') FROM " + src.getORID() + ") WHERE @rid IN ( ");
		sb.append("      SELECT @rid FROM ( ");
		sb.append("        TRAVERSE OUT('" + eAclRelation.class.getSimpleName() + "') FROM " + dst.getORID() + " ");
		sb.append("          ) ");
		sb.append("        WHERE @class = '" + vAcl.class.getSimpleName() + "' ");
		sb.append("      ) ");
		sb.append("    AND @class = '" + vAcl.class.getSimpleName() + "' ");
		sb.append("   )");
		sb.append(" ) WHERE @class IN ['" + vAclAdmin.class.getSimpleName() + "', '" + vAclData.class.getSimpleName() + "', '" + vAclOwner.class.getSimpleName() + "']");
		

		
		SecurityAcl sAcl = new SecurityAcl();

		for(OrientVertex ov : GraphFactory.command(null, sb.toString())) {

			sAcl.put(ov);
		}
		
		
		time = System.currentTimeMillis() - time;
		logger.info(String.format("Executed in %sms", time));
		
		return sAcl;
	}
	
	
	
	
	
}
