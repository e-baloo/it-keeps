
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jUser;

@DatabaseVertrex()
public final class fUser extends vBaseChildAcl {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(fUser.class);

	

	
	
	
	// API

	
	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminUserRead())
			throw ExceptionPermission.DENY;
		List<jBaseLight> list = vBase.getAllBase(null, vUser.class, false).stream().map(e -> e.read().getJBaseLight()).collect(Collectors.toList());
		return list;
	}

	
	
	
	public static final jUser read(Rid requesteurRid, Rid userRid) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, userRid);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		if ((!sAcl.isRoleRoot() || !sAcl.isRoleAdmin()) && !userRid.equals(requesteurRid))
			throw ExceptionPermission.IS_USER;
		if (!userRid.equals(requesteurRid) && !sAcl.isAdminUserRead())
			throw ExceptionPermission.NOT_USER_READ;

		vUser user = vUser.get(null, vUser.class, userRid, false);

		return user.read();
	}

	public static final jUser delete(Rid requesteurRid, Rid userRid) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, userRid);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		if ((!sAcl.isRoleRoot() || !sAcl.isRoleAdmin()) && !userRid.equals(requesteurRid))
			throw ExceptionPermission.IS_USER;
		if (!sAcl.isAdminUserCreate())
			throw ExceptionPermission.NOT_USER_CREATE;

		vUser user = vUser.get(null, vUser.class, userRid, false);

		jUser j = vUser.get(null, vUser.class, userRid, false).read();

		user.delete();

		return j;
	}

	public static final jUser create(Rid requesteurRid, jUser j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);

		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if (!sAcl.isAdminUserCreate())
			throw ExceptionPermission.NOT_USER_CREATE;

		checkUpdate(sAcl, j);

		vUser user = new vUser(j);

		return fUser.read(requesteurRid, user.getRid());
	}

	public static final jUser update(Rid requesteurRid, jUser j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, j.getRid());

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		if (!j.getRid().equals(requesteurRid) && !sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_USER;
		if (!j.getRid().equals(requesteurRid) && !sAcl.isAdminUserUpdate())
			throw ExceptionPermission.NOT_USER_UPDATE;

		checkUpdate(sAcl, j);

		vUser user = vUser.get(null, vUser.class, j.getRid(), false);

		user.checkVersion(j);
		user.update(j);

		return fUser.read(requesteurRid, j.getRid());

	}

	private static final void checkUpdate(SecurityAcl sAcl, final jUser j) {
		checkUpdateCredentials(j, sAcl);
		checkUpdateAclAdmin(j, sAcl);
		checkUpdateAclGroups(j, sAcl);
		checkUpdateRole(j, sAcl);
		checkUpdateGroups(j, sAcl);
	}

	private static final void checkUpdateAclGroups(final jUser j, SecurityAcl sAcl) {
		if (!j.isPresentAclGroups())
			return;
		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if (!sAcl.isAdminDelegate())
			throw ExceptionPermission.NOT_DELEGATE;
	}

	private static final void checkUpdateCredentials(final jUser j, SecurityAcl sAcl) {
		if (!j.isPresentCredentials())
			return;
	}

	private static final void checkUpdateAclAdmin(final jUser j, SecurityAcl sAcl) {
		if (!j.isPresentAclAdmin())
			return;
		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if (!sAcl.isAdminDelegate())
			throw ExceptionPermission.NOT_DELEGATE;
	}

	private static final void checkUpdateRole(final jUser j, SecurityAcl sAcl) {
		if (!j.isPresentRole())
			return;
		enAclRole role = j.getRole();
		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if (role.isRoot() && !sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;
		if (!sAcl.isAdminDelegate())
			throw ExceptionPermission.NOT_DELEGATE;
	}

	private static final void checkUpdateGroups(final jUser j, SecurityAcl sAcl) {
		if (!j.isPresentGroups())
			return;
		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			return;
		if (!sAcl.isAdminGroupUpdate())
			throw ExceptionPermission.NOT_GROUP_UPDATE;
	}

}
