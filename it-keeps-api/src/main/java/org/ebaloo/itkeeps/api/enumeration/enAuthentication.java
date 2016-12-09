package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAuthentication extends enAbstract<String> {

	private static final HashMap<String, enAuthentication> map = new HashMap<>();

	public static final enAuthentication BASIC = new enAuthentication("BASIC");
	public static final enAuthentication TOKEN = new enAuthentication("TOKEN");
	public static final enAuthentication LDAP = new enAuthentication("LDAP");
	public static final enAuthentication ACTIVE_DIRECTORY = new enAuthentication("ACTIVE_DIRECTORY");


	private enAuthentication(String name) {
		super(name, map.size(), name);
		map.put(name, this);
	}
	

	public static enAuthentication valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAuthentication> values() {
		return map.values();
	};
	
	

}