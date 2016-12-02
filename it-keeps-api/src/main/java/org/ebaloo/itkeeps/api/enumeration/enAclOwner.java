package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclOwner extends enAbstract<Boolean> {

	private static final HashMap<String, enAclOwner> map = new HashMap<String, enAclOwner>();

	public static final enAclOwner OWNER_FALSE = new enAclOwner("OWNER_FALSE", Boolean.FALSE);
	public static final enAclOwner OWNER_TRUE = new enAclOwner("OWNER_TRUE", Boolean.TRUE);

	private enAclOwner(String name, Boolean value) {
		super(name, map.size(), value);
		map.put(name, this);
	}

	public static enAclOwner valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclOwner> values() {
		return map.values();
	};
	
}
