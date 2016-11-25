package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclAdmin extends enAbstract<String> {

	private static final HashMap<String, enAclAdmin> map = new HashMap<String, enAclAdmin>();

	public static enAclAdmin DELEGATE =new enAclAdmin("DELEGATE");
	public static enAclAdmin NO_DELEGATE = new enAclAdmin("NO_DELEGATE");

	public static enAclAdmin GROUP_CREATE = new enAclAdmin("GROUP_CREATE");
	public static enAclAdmin GROUP_CREATE_ROOT = new enAclAdmin("GROUP_CREATE_ROOT");
	public static enAclAdmin GROUP_UPDATE = new enAclAdmin("GROUP_UPDATE");
	public static enAclAdmin GROUP_NO_OPERATION =  new enAclAdmin("GROUP_NO_OPERATION");
	
	public static enAclAdmin PATH_CREATE = new enAclAdmin("PATH_CREATE");
	public static enAclAdmin PATH_GREATE_ROOT = new enAclAdmin("PATH_GREATE_ROOT");
	public static enAclAdmin PATH_NO_CREATE =  new enAclAdmin("PATH_NO_CREATE");

	public static enAclAdmin ENTRY_NO_CREATE =  new enAclAdmin("PATH_NO_CREATE");

	private enAclAdmin(String name) {
		super(name, map.size(), name);
		map.put(name, this);
	}
	

	public static enAclAdmin valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclAdmin> values() {
		return map.values();
	};
	
}
