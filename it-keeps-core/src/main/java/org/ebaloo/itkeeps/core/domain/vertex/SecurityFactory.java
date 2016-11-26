package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ebaloo.itkeeps.Guid;
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
import org.ebaloo.itkeeps.core.domain.vertex.vBaseAbstract.WhereClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public final class SecurityFactory {

	public static class SecurityAcl {

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

			if (this.aclAdmin.contains(enAclAdmin.NO_DELEGATE))
				return false;

			return this.aclAdmin.contains(enAclAdmin.DELEGATE);
		}

		public boolean isAdminOwner() {
			return this.aclOwner.value();
		}

		public boolean isAdminUpdateGroup() {
			
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_UPDATE);
		}

		public boolean isAdminCreatGroup() {
			
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}

		public boolean isAdminCreatRootGroup() {
			
			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.GROUP_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.GROUP_CREATE_ROOT);
		}

		public boolean isAdminUserRead() {

			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_READ) || this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}
		
		public boolean isAdminUserUpdate() {

			if (this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

		public boolean isAdminUserCreate() {

			if(this.isAdminOwner())
				return true;

			if(this.isRoleRoot())
				return true;
			
			if(this.aclAdmin.contains(enAclAdmin.USER_NO_OPERATION))
				return false;
			
			return this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

		
		/*
		public boolean isDataCreate() {
			if (this.isAdminOwner())
				return true;

			return this.aclData.ordinal() <= enAclData.CREATE.ordinal();
		}

		public boolean isDataDeny() {
			if (this.isAdminOwner())
				return false;

			return this.aclData.ordinal() >= enAclData.DENY.ordinal();
		}

		public boolean isDataRead() {
			if (this.isAdminOwner())
				return true;

			return this.aclData.ordinal() <= enAclData.READ.ordinal();
		}

		public boolean isDataUpdate() {
			if (this.isAdminOwner())
				return true;

			return this.aclData.ordinal() <= enAclData.UPDATE.ordinal();
		}
		*/

		public boolean isRoleAdmin() {
			return aclRole.isAdmin();
		}

		public boolean isRoleGuest() {
			return this.aclRole.isGuest();
		}

		public boolean isRoleIn(enAclRole role) {
			return this.aclRole.isInRole(role);
		}

		public boolean isRoleRoot() {
			return aclRole.isRoot();
		}

		public boolean isRoleUser() {
			return aclRole.isUser();
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

		if (logger.isTraceEnabled())
			logger.trace("checkRootUserExist()");

		// TODO Add CONF "Create Root User"
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
	
	/*
	static SecurityAcl getSecurityAcl(OrientBaseGraph graph, final Guid guidUser, final vBaseChildAcl dst) {
		return SecurityFactory.getSecurityAcl(vUser.get(graph, vUser.class, guidUser, false), dst);
	}
	
	static SecurityAcl getSecurityAcl(final vUser src, final vBaseChildAcl dst) {
		return getSecurityAcl(src.getOrientVertex(), dst.getOrientVertex());
	}

	static SecurityAcl getSecurityAcl(final vUser src, final jBaseLight dst) {
		
		String cmd = "SELECT FROM vBase WHERE " + WhereClause.ENABLE_IS_TRUE + " AND guid = ?";
		
		
		return getSecurityAcl(src.getOrientVertex(), dst.getOrientVertex());
	}


	
	static OrientVertex getOrientVertex(jBaseLight jbl) {
		
		String cmdSQL = "SELECT FROM " + V_BASE_CLASS + " WHERE " + WhereClause.ENABLE_IS_TRUE + " AND (" + jBase.GUID + "= ?)";
		
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL, guid.toString());
		
	}

*/
	
	
	public static class oRID {
		
		public final static oRID NULL = new oRID(); 
		
		private final String orid;
		
		private oRID() {
			this.orid = null;
		}
		
		oRID(vBaseChildAcl base) {
			this.orid = base.getORID();
		}
		
		oRID(jBaseLight jbl) {
			this(jbl.getGuid());
		}
		
		private static final String REQUEST_GUID = "SELECT FROM " + vBase.class.getSimpleName() + " WHERE (" + jBase.GUID + "= ?)";

		
		oRID(Guid guid) {
			List<OrientVertex> list = GraphFactory.command(null, REQUEST_GUID, guid.toString());

			if(list.size() != 1)
				throw new RuntimeException(String.format("guid is zero or not unique (%s): %s", list.size() ,guid));

			this.orid = list.get(0).getIdentity().toString();
		}

		oRID(OrientVertex ov) {
			this.orid = ov.getIdentity().toString();
		}

		String get() {
			return this.orid;
		}
		
	}
	
		
	static SecurityAcl getSecurityAcl(final oRID src, final oRID dst) {

		long time = System.currentTimeMillis();

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

		time = System.currentTimeMillis() - time;
		logger.info(String.format("Executed in %sms", time));

		return sAcl;
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
		public static final RuntimeException IS_GUEST_OR_USER = new RuntimeException("requester is '" + enAclRole.GUEST.name() + "' or '" + enAclRole.USER.name() + "'");
		public static final RuntimeException IS_USER = new RuntimeException("requester is '" + enAclRole.USER.name() + "'");

		public static final RuntimeException NOT_ROOT = new RuntimeException("requester is not '" + enAclRole.ROOT.name() + "'");
		
		public static final RuntimeException NOT_DELEGATE =  new RuntimeException("requester have not '" + enAclAdmin.DELEGATE.name() + "' permission");
		public static final RuntimeException NOT_GROUP_UPDATE = new RuntimeException("requester have not '" + enAclAdmin.GROUP_UPDATE.name() + "' permission");
		public static final RuntimeException NOT_USER_CREATE = new RuntimeException("requester have not '" + enAclAdmin.USER_CREATE.name() + "' permission");
		
		
	}
	

}
