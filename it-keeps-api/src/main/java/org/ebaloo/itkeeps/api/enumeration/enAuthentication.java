package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAuthentication extends enAbstract<String> {

	private static final HashMap<String, enAuthentication> map = new HashMap<String, enAuthentication>();

	public static final enAuthentication BASIC = new enAuthentication("BASIC", 0);
	public static final enAuthentication TOKEN = new enAuthentication("TOKEN", 1);
	public static final enAuthentication LDAP = new enAuthentication("LDAP", 2);
	public static final enAuthentication ACTIVE_DIRECTORY = new enAuthentication("ACTIVE_DIRECTORY", 3);


	private enAuthentication(String name, int ordinal) {
		super(name, ordinal, name);
		map.put(name, this);
	}
	

	public static enAuthentication valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAuthentication> values() {
		return map.values();
	};
	
	

}