package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclData extends enAbstract<String> {

	private static final HashMap<String, enAclData> map = new HashMap<String, enAclData>();

	public static final enAclData PATH_CREATE = new enAclData("PATH_CREATE");
	public static final enAclData PATH_UPDATE = new enAclData("PATH_UPDATE");
	public static final enAclData PATH_READ = new enAclData("PATH_READ");
	public static final enAclData PATH_DENY = new enAclData("PATH_DENY");

	public static final enAclData ENTRY_CREATE = new enAclData("ENTRY_CREATE");
	public static final enAclData ENTRY_UPDATE = new enAclData("ENTRY_UPDATE");
	public static final enAclData ENTRY_READ = new enAclData("ENTRY_READ");
	public static final enAclData ENTRY_DENY = new enAclData("ENTRY_DENY");

	public static final enAclData DENY = new enAclData("DENY");

	
	private enAclData(String name) {
		super(name, map.size(), name);
		map.put(name, this);
	}
	

	public static enAclData valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclData> values() {
		return map.values();
	};
	
}
