package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.HashSet;
import java.util.Set;

import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;
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
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) AS COUNT FROM (TRAVERSE OUT('");
			sb.append(E_ACL_NO_TRAVERSE);
			sb.append("') FROM (SELECT FROM ");
			sb.append(vUser.class.getSimpleName());
			sb.append(" WHERE ");
			sb.append(vBase.WhereClause.ENABLE_IS_TRUE);
			sb.append(")) WHERE @class = '");
			sb.append(V_ACL_ROLE);
			sb.append("' AND name = '");
			sb.append(enAclRole.ROOT.name());
			sb.append("'");
			
		Long rootUserCount = GraphFactory.command(null, sb.toString()).get(0).getProperty("COUNT");
		logger.trace("rootUserCount : " + rootUserCount.toString());
		
		if(rootUserCount == 0) {
			vCredential cred = new vCredential(jcredential, null);
			cred.commit();
			vUser user = cred.getUser();
			user.commit();
			user.reload();
			user.setRole(enAclRole.ROOT);
			
			
			logger.trace("checkRootUserExist() cred :" + cred.toString());
			logger.trace("checkRootUserExist() user :" + user.toString() + " / " + user.getRole());
			
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
		private enAclRole aclRole = null;
		
		public boolean isOwner() {
			return this.aclOwner.value();
		}
		
		public boolean isDataDeny() {
			
			if(this.isOwner())
				return false;
			
			return this.aclData.ordinal() >= enAclData.DENY.ordinal();
		}
		
		public boolean isDataRead() {
			
			if(this.isOwner())
				return true;
			
			return this.aclData.ordinal() <= enAclData.READ.ordinal();
		}
		
		public boolean isDataUpdate() {
			
			if(this.isOwner())
				return true;
			
			return this.aclData.ordinal() <= enAclData.UPDATE.ordinal();
		}
		
		public boolean isDataCreate() {
			
			if(this.isOwner())
				return true;
			
			return this.aclData.ordinal() <= enAclData.CREATE.ordinal();
		}


		public boolean isAdminDeleguat() {

			if(this.isOwner())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.NO_DELEGATE))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.DELEGATE);
		}
		
		
		public boolean isInRole(enAclRole role) {
			return this.aclRole.isInRole(role); 
		}
		
		public boolean isGuest() {
			return this.aclRole.isGuest();
		}
		
		public boolean isUser() {
			return aclRole.isUser();
		}

		public boolean isAdmin() {
			return aclRole.isAdmin();
		}

		public boolean isRoot() {
			return aclRole.isRoot();
		}
		
	}
	
	
	private static final String V_ACL_OWNER = vAclOwner.class.getSimpleName();
	private static final String V_ACL_ADMIN = vAclAdmin.class.getSimpleName();
	private static final String V_ACL_DATA = vAclData.class.getSimpleName();
	private static final String V_ACL_ROLE = vAclRole.class.getSimpleName();
	private static final String V_ACL_ENUM = vAclEnum.class.getSimpleName();
	private static final String E_ACL_NO_TRAVERSE = eAclNoTraverse.class.getSimpleName();
	private static final String E_ACL_RELATION = eAclRelation.class.getSimpleName();
	
	public static SecurityAcl getSecurityAcl(final vUser src, final vBaseChildAcl dst) {
		
		long time = System.currentTimeMillis();
		

		if(src == null) 
			throw new RuntimeException("TODO"); // TODO

		SecurityAcl sAcl = new SecurityAcl();

		
		String userOrid = src.getORID();


		{
			StringBuilder request = new StringBuilder();
		
			request.append("SELECT FROM (TRAVERSE OUT('");
			request.append(E_ACL_NO_TRAVERSE);
			request.append("') FROM ");
			request.append(userOrid);
			request.append(" WHILE $depth <= 1) WHERE @class INSTANCEOF '");
			request.append(V_ACL_ENUM);
			request.append("'");
	
			for(OrientVertex ov : GraphFactory.command(null, request.toString())) {
				put(sAcl, ov);
			}
		}
		
		if(dst != null) {
			StringBuilder request =  new StringBuilder();
	
			request.append("SELECT FROM (TRAVERSE OUT('");
			request.append(E_ACL_NO_TRAVERSE);
			request.append("') FROM (SELECT FROM (TRAVERSE OUT('");
			request.append(E_ACL_RELATION);
			request.append("') FROM ");
			request.append(userOrid);
			request.append(") WHERE @rid IN (SELECT @rid FROM (TRAVERSE OUT('");
			request.append(E_ACL_RELATION);
			request.append("') FROM ");
			request.append(dst.getORID());
			request.append(") WHERE @class = '");
			request.append(vAcl.class.getSimpleName());
			request.append("') AND @class = '");
			request.append(vAcl.class.getSimpleName());
			request.append("')) WHERE @class INSTANCEOF '");
			request.append(V_ACL_ENUM);
			request.append("'");
		
			for(OrientVertex ov : GraphFactory.command(null, request.toString())) {
				put(sAcl, ov);
			}

		}
		
		
		time = System.currentTimeMillis() - time;
		logger.info(String.format("Executed in %sms", time));
		
		return sAcl;
	}
	
	
	private static void put(SecurityAcl sAcl, OrientVertex ov) {
		
		String type = ov.getType().getName();
		
		if(V_ACL_OWNER.equals(type)) {
			if(sAcl.aclOwner.value() == Boolean.TRUE)
				return;
			
			sAcl.aclOwner = enAclOwner.valueOf(ov.getProperty(vAclOwner.NAME));
			return;
		}

		if(V_ACL_DATA.equals(type)) {
			enAclData tAclData = enAclData.valueOf(ov.getProperty(vAclData.NAME));
			
			if(sAcl.aclData == null ) {
				sAcl.aclData = tAclData;
				return;
			}
			
			if(sAcl.aclData.ordinal() < tAclData.ordinal()) {
				sAcl.aclData = tAclData;
			}
			return;
		}
		
		if(V_ACL_ADMIN.equals(type)) {
			sAcl.aclAdmin.add(enAclAdmin.valueOf(ov.getProperty(vAclAdmin.NAME)));
			return;
		}

		if(V_ACL_ROLE.equals(type)) {

			if(sAcl.aclRole != null)
				throw new RuntimeException("To many role defined!");
			
			sAcl.aclRole = enAclRole.valueOf(ov.getProperty(vAclRole.NAME));
			return;
		}

		throw new RuntimeException("The type : " + type + " is unknow !");
	}
	
	
	
	
}
