package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class AuthenticationType extends EnumAbstract<String> {

	private static final HashMap<String, AuthenticationType> map = new HashMap<String, AuthenticationType>();

	public static final AuthenticationType BASIC = new AuthenticationType("BASIC", 0);
	public static final AuthenticationType TOKEN = new AuthenticationType("TOKEN", 1);
	public static final AuthenticationType LDAP = new AuthenticationType("LDAP", 2);
	public static final AuthenticationType ACTIVE_DIRECTORY = new AuthenticationType("ACTIVE_DIRECTORY", 3);


	private AuthenticationType(String name, int ordinal) {
		super(name, ordinal, name);
		map.put(name, this);
	}
	

	public static AuthenticationType valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<AuthenticationType> values() {
		return map.values();
	};
	
	

}