package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAuthenticationType extends enAbstract<String> {

	private static final HashMap<String, enAuthenticationType> map = new HashMap<>();

	public static final enAuthenticationType BASIC = new enAuthenticationType("BASIC");
	public static final enAuthenticationType TOKEN = new enAuthenticationType("TOKEN");
	public static final enAuthenticationType LDAP = new enAuthenticationType("LDAP");
	public static final enAuthenticationType ACTIVE_DIRECTORY = new enAuthenticationType("ACTIVE_DIRECTORY");


	private enAuthenticationType(String name) {
		super(name, map.size(), name);
		map.put(name, this);
	}
	

	public static enAuthenticationType valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAuthenticationType> values() {
		return map.values();
	};
	
	

}