package org.ebaloo.itkeeps.core.domain.vertex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public final class SecurityFactory {


	public static final Timer TIMER_GET_SECURITY_ACL = MetricsFactory.getMetricRegistry().timer(SecurityFactory.class.getName() + ".getSecurityAcl");
	private static final Logger logger = LoggerFactory.getLogger(SecurityFactory.class.getName());
	private static final String V_ACL_OWNER = vAclOwner.class.getSimpleName();
	private static final String V_ACL_ADMIN = vAclAdmin.class.getSimpleName();
	private static final String V_ACL_DATA = vAclData.class.getSimpleName();
	private static final String V_ACL_ROLE = vAclRole.class.getSimpleName();
	private static final String V_ACL_ENUM = vAclEnum.class.getSimpleName();
	private static final String E_ACL_NO_TRAVERSE = eAclNoTraverse.class.getSimpleName();
	private static final String E_ACL_RELATION = eAclRelation.class.getSimpleName();
	private static MessageDigest messageDigest = null;
	private static StandardPBEStringEncryptor entriEncryptor = null;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static void checkRootUserExist(jCredential jcredential) {

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
				vUser user = cred._getUser();
				user.setRole(enAclRole.ROOT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static SecurityAcl getSecurityAcl(final Rid src, final Rid dst) {

		final Timer.Context timerContext = TIMER_GET_SECURITY_ACL.time();

		try {

			if (src == null)
				throw new RuntimeException("TODO"); // TODO

			SecurityAcl sAcl = new SecurityAcl();

			String userOrid = src.getFull();

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

			if ((dst != null) && (dst.getFull() != null)) {
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
				request.append(dst.getFull());
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

	public static void validateCredential(jCredential credential) {

		if (logger.isTraceEnabled())
			logger.trace("validateCredential()");

		checkRootUserExist(credential);

		// TODO
	}

	/*
	public static void validateCredential(vUser user, jCredential jcredential) {

		if (logger.isTraceEnabled())
			logger.trace("validateCredential()");

		vCredential credential = vCredential.get(null, vCredential.class, jcredential.getCred(), false);

		if (credential == null) {
			throw new RuntimeException("TODO"); // TODO
		}

		if (!user.equals(credential._getUser()))
			throw new RuntimeException("TODO"); // TODO

		enAuthentication authentication = credential.getAuthenticationType();

		if (authentication == null) {
			throw new RuntimeException("TODO"); // TODO
		}

		if (credential.getAuthenticationType().equals(enAuthentication.BASIC)) {
			;
		} else {
			throw new RuntimeException("TODO"); // TODO
		}

		// TODO
	}
	*/

	private static MessageDigest getMessageDigest() {
		if (messageDigest == null) {
			try {
				messageDigest = MessageDigest.getInstance("SHA-512", "BC");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return messageDigest;
	}


	// ------------------------------------

	public static String getHash(String base64) {
		return Base64.encodeBase64String(_getHash(base64));
	}

	private static byte[] _getHash(String base64) {
		if (StringUtils.isEmpty(base64))
			return null;

		byte[] data = Base64.decodeBase64(base64);
		return getMessageDigest().digest(data);
	}

	public static boolean checkHash(String base64, String hash64) {
		if (StringUtils.isEmpty(base64) || StringUtils.isEmpty(hash64))
			return false;

		byte[] digesta = _getHash(base64);
		byte[] digestb = Base64.decodeBase64(hash64);
		return MessageDigest.isEqual(digesta, digestb);
	}

	static StringEncryptor getEntryEncryptor() {

		if (entriEncryptor == null) {

			try {

				entriEncryptor = new StandardPBEStringEncryptor();

				entriEncryptor.setProvider(new BouncyCastleProvider());

                String provider = ConfigFactory.getString("encrypt.provider", "BC");
                String algorithm = ConfigFactory.getString("encrypt.algorithm", "PBEWITHSHA256AND128BITAES-CBC-BC");
                String password64 = ConfigFactory.getString("encrypt.password64",
                        "Q15KWVdrJlBhNSZSWnJuYkRhNnpYUCE3LXZ6UTgtYnY=");
                int iterations = ConfigFactory.getInt("encrypt.iterations", 500);

				String password = new String(Base64.decodeBase64(password64), "UTF-8");

				entriEncryptor.setProviderName(provider);
				entriEncryptor.setAlgorithm(algorithm);
				entriEncryptor.setPassword(password);
				entriEncryptor.setKeyObtentionIterations(iterations);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
		return entriEncryptor;
	}

	public final static class SecurityAcl {

		private enAclOwner aclOwner = enAclOwner.OWNER_FALSE;
		private Set<enAclData> aclData = new HashSet<>();
		private Set<enAclAdmin> aclAdmin = new HashSet<>();
		private enAclRole aclRole = null;


		public String toString() {

			return "[" +
					SecurityAcl.class.getSimpleName() +
					":" +
					aclRole +
					":" +
					aclOwner +
					":" +
					aclAdmin +
					":" +
					aclData +
					"]";
		}


        boolean isAdminDelegate() {

			if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if (this.aclAdmin.contains(enAclAdmin.DELEGATE_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.DELEGATE);
		}

        boolean isOwner() {
            return this.aclOwner.value();
		}

        boolean isAdminGroupUpdate() {

			if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.GROUP_UPDATE) || this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}

        boolean isAdminGroupCreate() {

			if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}

        boolean isAdminUserRead() {

			if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.USER_READ) || this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

        boolean isAdminUserUpdate() {

			if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.USER_UPDATE) || this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}

        boolean isAdminUserCreate() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.USER_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.USER_CREATE);
		}


        boolean isRoleAdmin() {
            return aclRole.isAdmin();
		}

        boolean isRoleGuest() {
            return this.aclRole.isGuest();
		}

        boolean isRoleRoot() {
            return aclRole.isRoot();
		}

        boolean isRoleUser() {
            return aclRole.isUser();
		}


        boolean isDataPathRead() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE) || this.aclData.contains(enAclData.PATH_UPDATE) || this.aclData.contains(enAclData.PATH_READ);
		}

        boolean isDataPathUpdate() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE) || this.aclData.contains(enAclData.PATH_UPDATE);
		}

        boolean isDataPathCreate() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE);
		}


        boolean isDataEntryRead() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY))
				return false;

			return this.aclData.contains(enAclData.ENTRY_CREATE) || this.aclData.contains(enAclData.ENTRY_UPDATE) || this.aclData.contains(enAclData.ENTRY_READ);
		}

		public boolean isDataEntryUpdate() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY))
				return false;

			return (this.aclData.contains(enAclData.ENTRY_CREATE) || this.aclData.contains(enAclData.ENTRY_UPDATE));
		}

		public boolean isDataEntryCreate() {

			if(this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.PATH_DENY))
				return false;

			return this.aclData.contains(enAclData.PATH_CREATE);
		}


        boolean isAdminAclRead() {

				if (this.isOwner())
					return true;

				if(this.isRoleRoot())
					return true;

			if (this.aclAdmin.contains(enAclAdmin.ACL_DENY))
					return false;

			return this.aclAdmin.contains(enAclAdmin.ACL_READ) || this.aclAdmin.contains(enAclAdmin.ACL_UPDATE) || this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
		}


        boolean isAdminGroupRead() {
            if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.GROUP_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.GROUP_READ) || this.aclAdmin.contains(enAclAdmin.GROUP_UPDATE) || this.aclAdmin.contains(enAclAdmin.GROUP_CREATE);
		}


        boolean isAdminAclCreate() {
            if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.ACL_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
		}


        boolean isAdminAclUpdate() {
            if (this.isOwner())
				return true;

			if(this.isRoleRoot())
				return true;

			if(this.aclAdmin.contains(enAclAdmin.ACL_DENY))
				return false;

			return this.aclAdmin.contains(enAclAdmin.ACL_UPDATE) || this.aclAdmin.contains(enAclAdmin.ACL_CREATE);
		}


        boolean isDataEncryptedEntryRead() {
            if(this.isOwner())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY) || this.aclData.contains(enAclData.ENCRYPTED_ENTRY_DENY))
				return false;

			return this.aclData.contains(enAclData.ENCRYPTED_ENTRY_READ) || this.aclData.contains(enAclData.ENCRYPTED_ENTRY_UPDATE);
		}

        boolean isDataEncryptedEntryUpdate() {
            if(this.isOwner())
				return true;

			if(this.aclData.contains(enAclData.DENY) || this.aclData.contains(enAclData.ENTRY_DENY) || this.aclData.contains(enAclData.ENCRYPTED_ENTRY_DENY))
				return false;

			return this.aclData.contains(enAclData.ENCRYPTED_ENTRY_UPDATE);
		}

	}

	// ------------------------------------

    static class RID {

		static Rid get(vBaseChildAcl base) {
			return base.getRid();
		}

		static Rid get(OrientVertex ov) {
			return new Rid(ov.getIdentity().toString());
		}

		static Rid get(jBaseLight j) {
			return j.getId();
		}

		static <T extends jBase> Rid get(jBase j) {
			return j.getRid();
		}

		static Rid get(Rid rid) {
			return rid;
		}

	}

    static final class ExceptionPermission {


        static final RuntimeException IS_GUEST = new RuntimeException("requester is '" + enAclRole.GUEST.name() + "'");

		// TODO remove
        static final RuntimeException IS_GUEST_OR_USER = new RuntimeException("requester is '" + enAclRole.GUEST.name() + "' or '" + enAclRole.USER.name() + "'");
        // TODO remove
        static final RuntimeException IS_USER = new RuntimeException("requester is '" + enAclRole.USER.name() + "'");

        static final RuntimeException NOT_ROOT = new RuntimeException("requester is not '" + enAclRole.ROOT.name() + "'");
        static final RuntimeException NOT_ADMIN = new RuntimeException("requester is not '" + enAclRole.ADMIN.name() + "'");
        static final RuntimeException NOT_USER = new RuntimeException("requester is not '" + enAclRole.USER.name() + "'");

        static final RuntimeException NOT_DELEGATE = new RuntimeException("requester have not '" + enAclAdmin.DELEGATE.name() + "' permission");
        static final RuntimeException NOT_GROUP_UPDATE = new RuntimeException("requester have not '" + enAclAdmin.GROUP_UPDATE.name() + "' permission");
        static final RuntimeException NOT_USER_CREATE = new RuntimeException("requester have not '" + enAclAdmin.USER_CREATE.name() + "' permission");
        static final RuntimeException NOT_USER_UPDATE = new RuntimeException("requester have not '" + enAclAdmin.USER_UPDATE.name() + "' permission");
        static final RuntimeException NOT_USER_READ = new RuntimeException("requester have not '" + enAclAdmin.USER_READ.name() + "' permission");

        static final RuntimeException DENY = new RuntimeException("requester have '" + enAclData.DENY.name() + "'");
        ;


	}
	
}
