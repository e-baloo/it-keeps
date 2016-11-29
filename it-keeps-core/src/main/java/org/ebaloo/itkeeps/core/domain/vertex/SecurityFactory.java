package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.HashSet;
import java.util.Set;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public final class SecurityFactory {

	public final static class SecurityAcl {

		private enAclOwner aclOwner = enAclOwner.FALSE;
		private Set<enAclData> aclData = new HashSet<enAclData>();
		private Set<enAclAdmin> aclAdmin = new HashSet<enAclAdmin>();
		private enAclRole aclRole = null;

		
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("[");
			sb.append(SecurityAcl.class.getSimpleName());
			sb.append(":");
			sb.append(aclRole);
			sb.append(":");
			sb.append(aclOwner);
			sb.append(":");
			sb.append(aclAdmin);
			sb.append(":");
			sb.append(aclData);
			sb.append("]");
			
			return sb.toString();
		}
		
		
		public boolean isAdminDelegate() {
			
			if (this.isAdminOwner())
				return true;
			
			if(this.isRoleRoot())
				return true;

			if (this.aclAdmin.contains(enAclAdmin.DELEGATE_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.DELEGATE);
		}

		public boolean isAdminOwner() {
			return this.aclOwner.value();
		}

		public boolean isAdminGroupUpdate() {
			
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_UPDATE) || this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}

		public boolean isAdminGroupCreate() {
			
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}

		public boolean isAdminUserRead() {

			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_READ) || this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}
		
		public boolean isAdminUserUpdate() {

			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

		public boolean isAdminUserCreate() {

			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

		
		public boolean isRoleAdmin() {
			return aclRole.isAdmin();
		}

		public boolean isRoleGuest() {
			return this.aclRole.isGuest();
		}

		public boolean isRoleRoot() {
			return aclRole.isRoot();
		}

		public boolean isRoleUser() {
			return aclRole.isUser();
		}


		public boolean isDataPathRead() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE) || this.aclData.contains(enAclData.PATH_UPDATE) || this.aclData.contains(enAclData.PATH_READ);
		}

		public boolean isDataPathUpdate() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE) || this.aclData.contains(enAclData.PATH_UPDATE);
		}
		
		public boolean isDataPathCreate() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE);
		}


		public boolean isDataEntryRead() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY))
				return false;

			return this.aclData.contains(enAclData.ENTRY_CREATE) || this.aclData.contains(enAclData.ENTRY_UPDATE) || this.aclData.contains(enAclData.ENTRY_READ);
		}

		public boolean isDataEntryUpdate() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY))
				return false;

			return (this.aclData.contains(enAclData.ENTRY_CREATE) || this.aclData.contains(enAclData.ENTRY_UPDATE));
		}
		
		public boolean isDataEntryCreate() {
			
			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE);
		}



		public boolean isAdminAclRead() {

				if (this.isAdminOwner())
					return true;

				if(this.isRoleRoot())
					return true;
				
				if(this.aclAdmin.contains(enAclAdmin.ACL_DENY))
					return false;
				
				return this.aclAdmin.contains(enAclAdmin.ACL_READ) || this.aclAdmin.contains(enAclAdmin.ACL_UPDATE) || this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
			}		


		public boolean isAdminGroupRead() {
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_READ) || this.aclAdmin.contains(enAclAdmin.GROUP_UPDATE) || this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}


		public boolean isAdminAclCreate() {
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.ACL_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
		}


		public boolean isAdminAclUpdate() {
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.ACL_DENY))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.ACL_UPDATE) || this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
		}

	}

	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	private static final String V_ACL_OWNER = vAclOwner.class.getSimpleName();
	private static final String V_ACL_ADMIN = vAclAdmin.class.getSimpleName();
	private static final String V_ACL_DATA = vAclData.class.getSimpleName();
	private static final String V_ACL_ROLE = vAclRole.class.getSimpleName();
	private static final String V_ACL_ENUM = vAclEnum.class.getSimpleName();
	private static final String E_ACL_NO_TRAVERSE = eAclNoTraverse.class.getSimpleName();
	private static final String E_ACL_RELATION = eAclRelation.class.getSimpleName();
	
	
	
	private static final void checkRootUserExist(jCredential jcredential) {

		try {

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) AS COUNT FROM (TRAVERSE OUT('");
			sb.append(E_ACL_NO_TRAVERSE);
			sb.append("') FROM (SELECT FROM ");
			sb.append(vUser.class.getSimpleName());
			sb.append(")) WHERE @class = '");
			sb.append(V_ACL_ROLE);
			sb.append("' AND name = '");
			sb.append(enAclRole.ROOT.name());
			sb.append("'");

			Long rootUserCount = GraphFactory.command(null, sb.toString()).get(0).getProperty("COUNT");

			if (rootUserCount == 0) {
				logger.warn("Create 'root' User!");
				vCredential cred = new vCredential(jcredential, null);
				vUser user = cred.getUser();
				user.setRole(enAclRole.ROOT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public static class RID {
		
		static Rid get(vBaseChildAcl base) {
			return base.getRid();
		}
		
		static Rid get(OrientVertex ov) {
			return new Rid(ov.getIdentity().toString());
		}

		static Rid get(jBaseLight j) {
			return j.getRid();
		}

		static <T extends jBase> Rid get(jBase j) {
			return j.getRid();
		}

		static Rid get(Rid rid) {
			return rid;
		}
		
	}
	
	public static final Timer TIMER_GET_SECURITY_ACL = MetricsFactory.getMetricRegistry().timer(SecurityFactory.class.getName() + ".getSecurityAcl");
		
	static SecurityAcl getSecurityAcl(final Rid src, final Rid dst) {

		final Timer.Context timerContext = TIMER_GET_SECURITY_ACL.time();

		try {

			if (src == null)
				throw new RuntimeException("TODO"); // TODO
	
			SecurityAcl sAcl = new SecurityAcl();
	
			String userOrid = src.get();
	
			{
				StringBuilder request = new StringBuilder();
	
				request.append("SELECT FROM (TRAVERSE OUT('");
				request.append(E_ACL_NO_TRAVERSE);
				request.append("') FROM ");
				request.append(userOrid);
				request.append(" WHILE $depth <= 1) WHERE @class INSTANCEOF '");
				request.append(V_ACL_ENUM);
				request.append("'");
	
				for (OrientVertex ov : GraphFactory.command(null, request.toString())) {
					put(sAcl, ov);
				}
			}
	
			if ((dst != null) && (dst.get() != null)) {
				StringBuilder request = new StringBuilder();
	
				request.append("SELECT FROM (TRAVERSE OUT('");
				request.append(E_ACL_NO_TRAVERSE);
				request.append("') FROM (SELECT FROM (TRAVERSE OUT('");
				request.append(E_ACL_RELATION);
				request.append("') FROM ");
				request.append(userOrid);
				request.append(") WHERE @rid IN (SELECT @rid FROM (TRAVERSE OUT('");
				request.append(E_ACL_RELATION);
				request.append("') FROM ");
				request.append(dst.get());
				request.append(") WHERE @class = '");
				request.append(vAcl.class.getSimpleName());
				request.append("') AND @class = '");
				request.append(vAcl.class.getSimpleName());
				request.append("')) WHERE @class INSTANCEOF '");
				request.append(V_ACL_ENUM);
				request.append("'");
	
				for (OrientVertex ov : GraphFactory.command(null, request.toString())) {
					put(sAcl, ov);
				}
			}

			return sAcl;
			
		} finally {
			timerContext.stop();
		}

	}

	private static void put(SecurityAcl sAcl, OrientVertex ov) {

		String type = ov.getType().getName();

		if (V_ACL_OWNER.equals(type)) {
			if (sAcl.aclOwner.value() == Boolean.TRUE)
				return;

			sAcl.aclOwner = enAclOwner.valueOf(ov.getProperty(vAclOwner.NAME));
			return;
		}

		if (V_ACL_DATA.equals(type)) {
			sAcl.aclData.add(enAclData.valueOf(ov.getProperty(vAclData.NAME)));
			return;
		}

		if (V_ACL_ADMIN.equals(type)) {
			sAcl.aclAdmin.add(enAclAdmin.valueOf(ov.getProperty(vAclAdmin.NAME)));
			return;
		}

		if (V_ACL_ROLE.equals(type)) {

			if (sAcl.aclRole != null)
				throw new RuntimeException("To many role defined!");

			sAcl.aclRole = enAclRole.valueOf(ov.getProperty(vAclRole.NAME));
			return;
		}

		throw new RuntimeException("The type : " + type + " is unknow !");
	}

	public static final void validateCredential(jCredential credential) {

		if (logger.isTraceEnabled())
			logger.trace("validateCredential()");

		checkRootUserExist(credential);

		// TODO
	}

	public static void validateCredential(vUser user, jCredential jcredential) {

		if (logger.isTraceEnabled())
			logger.trace("validateCredential()");

		vCredential credential = vCredential.get(null, vCredential.class, jcredential.getId(), false);

		if (!user.equals(credential.getUser()))
			throw new RuntimeException("TODO"); // TODO

		if (credential.getAuthenticationType().equals(enAuthentication.BASIC)) {
			;
		} else {
			throw new RuntimeException("TODO"); // TODO
		}

		// TODO
	}
	
	
	public static final class ExceptionPermission {
		

		public static final RuntimeException IS_GUEST = new RuntimeException("requester is '" + enAclRole.GUEST.name() + "'");
		
		// TODO remove
		public static final RuntimeException IS_GUEST_OR_USER = new RuntimeException("requester is '" + enAclRole.GUEST.name() + "' or '" + enAclRole.USER.name() + "'");
		// TODO remove
		public static final RuntimeException IS_USER = new RuntimeException("requester is '" + enAclRole.USER.name() + "'");

		public static final RuntimeException NOT_ROOT = new RuntimeException("requester is not '" + enAclRole.ROOT.name() + "'");
		public static final RuntimeException NOT_ADMIN = new RuntimeException("requester is not '" + enAclRole.ADMIN.name() + "'");
		public static final RuntimeException NOT_USER = new RuntimeException("requester is not '" + enAclRole.USER.name() + "'");
		
		public static final RuntimeException NOT_DELEGATE =  new RuntimeException("requester have not '" + enAclAdmin.DELEGATE.name() + "' permission");
		public static final RuntimeException NOT_GROUP_UPDATE = new RuntimeException("requester have not '" + enAclAdmin.GROUP_UPDATE.name() + "' permission");
		public static final RuntimeException NOT_USER_CREATE = new RuntimeException("requester have not '" + enAclAdmin.USER_CREATE.name() + "' permission");
		public static final RuntimeException NOT_USER_UPDATE = new RuntimeException("requester have not '" + enAclAdmin.USER_UPDATE.name() + "' permission");
		public static final RuntimeException NOT_USER_READ = new RuntimeException("requester have not '" + enAclAdmin.USER_READ.name() + "' permission");

		public static final RuntimeException DENY = new RuntimeException("requester have '" + enAclData.DENY.name() + "'");;
		
		
	}
	

}
