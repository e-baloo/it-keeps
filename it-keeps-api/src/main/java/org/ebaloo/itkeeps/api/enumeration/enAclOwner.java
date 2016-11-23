package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclOwner extends enAbstract<Boolean> {

	private static final HashMap<String, enAclOwner> map = new HashMap<String, enAclOwner>();

	public static final enAclOwner FALSE = new enAclOwner("FASE", 0, Boolean.FALSE);
	public static final enAclOwner TRUE = new enAclOwner("TRUE", 1, Boolean.TRUE);

	private enAclOwner(String name, int ordinal, Boolean value) {
		super(name, ordinal, value);
		map.put(name, this);
	}

	public static enAclOwner valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclOwner> values() {
		return map.values();
	};
	
}
