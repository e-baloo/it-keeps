package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclData extends enAbstract<String> {

	private static final HashMap<String, enAclData> map = new HashMap<String, enAclData>();

	public static final enAclData CREATE = new enAclData("CREATE", 0);
	public static final enAclData UPDATE = new enAclData("UPDATE", 1);
	public static final enAclData READ = new enAclData("READ", 2);
	public static final enAclData DENY = new enAclData("DENY", 3);

	private enAclData(String name, int ordinal) {
		super(name, ordinal, name);
		map.put(name, this);
	}
	

	public static enAclData valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclData> values() {
		return map.values();
	};
	
}
