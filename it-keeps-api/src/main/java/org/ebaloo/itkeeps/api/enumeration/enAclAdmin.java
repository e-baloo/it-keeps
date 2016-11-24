package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclAdmin extends enAbstract<Long> {

	private static final HashMap<String, enAclAdmin> map = new HashMap<String, enAclAdmin>();

	public static enAclAdmin DELEGATE =new enAclAdmin("DELEGATE", 0x00000001L);
	public static enAclAdmin NO_DELEGATE = new enAclAdmin("NO_DELEGATE", 0x00000002L);

	public static enAclAdmin GROUP_CREATE = new enAclAdmin("GROUP_CREATE", 0x00000010L);
	public static enAclAdmin GROUP_CREATE_ROOT = new enAclAdmin("GROUP_CREATE_ROOT", 0x00000020L);
	public static enAclAdmin GROUP_NO_CREATE =  new enAclAdmin("PATH_NO_CREATE", 0x00000040L);
	
	public static enAclAdmin PATH_CREATE = new enAclAdmin("PATH_CREATE", 0x00000100L);
	public static enAclAdmin PATH_GREATE_ROOT = new enAclAdmin("PATH_GREATE_ROOT", 0x00000200L);
	public static enAclAdmin PATH_NO_CREATE =  new enAclAdmin("PATH_NO_CREATE", 0x00000400L);

	public static enAclAdmin ENTRY_NO_CREATE =  new enAclAdmin("PATH_NO_CREATE", 0x00004000L);

	private enAclAdmin(String name, long l) {
		super(name, map.size(), l);
		map.put(name, this);
	}
	

	public static enAclAdmin valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclAdmin> values() {
		return map.values();
	};
	
}
