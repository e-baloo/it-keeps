package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enRole;

public class enAclRole extends enAbstract<enRole> {

	private static final HashMap<String, enAclRole> map = new HashMap<>();

	public static final enAclRole ROOT = new enAclRole(enRole.ROOT);
	public static final enAclRole ADMIN = new enAclRole(enRole.ADMIN);
	public static final enAclRole USER = new enAclRole(enRole.USER);
	public static final enAclRole GUEST = new enAclRole(enRole.GUEST);

	private enAclRole(enRole role) {
		super(role.name(), role.ordinal(), role);
		map.put(role.name(), this);
	}

	public static enAclRole valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclRole> values() {
		return map.values();
	};
	
	public boolean isInRole(enAclRole role) {
		return this.ordinal() >= role.ordinal(); 
	}
	
	public boolean isGuest() {
		return this.ordinal() == GUEST.ordinal();
	}
	
	public boolean isUser() {
		return this.ordinal() == USER.ordinal() || this.ordinal() == ADMIN.ordinal() || this.ordinal() == ROOT.ordinal();
	}

	public boolean isAdmin() {
		return this.ordinal() == ADMIN.ordinal() || this.ordinal() == ROOT.ordinal();
	}

	public boolean isRoot() {
		return this.ordinal() == ROOT.ordinal();
	}
}
