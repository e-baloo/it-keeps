package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclAdmin extends enAbstract<String> {

	private static final HashMap<String, enAclAdmin> map = new HashMap<String, enAclAdmin>();


	public static final enAclAdmin DELEGATE =new enAclAdmin("DELEGATE");
	public static final enAclAdmin DELEGATE_DENY = new enAclAdmin("DELEGATE_DENY");

	public static final enAclAdmin GROUP_CREATE = new enAclAdmin("GROUP_CREATE");
	public static final enAclAdmin GROUP_UPDATE = new enAclAdmin("GROUP_UPDATE");
	public static final enAclAdmin GROUP_READ = new enAclAdmin("GROUP_READ");
	public static final enAclAdmin GROUP_DENY =  new enAclAdmin("GROUP_DENY");

	public static final enAclAdmin USER_CREATE = new enAclAdmin("USER_CREATE");
	public static final enAclAdmin USER_UPDATE = new enAclAdmin("USER_UPDATE");
	public static final enAclAdmin USER_READ = new enAclAdmin("USER_READ");
	public static final enAclAdmin USER_DENY = new enAclAdmin("USER_DENY");

	public static final enAclAdmin ACL_GROUP_UPDATE = new enAclAdmin("ACL_GROUP_UPDATE");
	public static final enAclAdmin ACL_GROUP_READ = new enAclAdmin("ACL_GROUP_READ");
	public static final enAclAdmin ACL_GROUP_DENY = new enAclAdmin("ACL_GROUP_DENY");


	public static final enAclAdmin ACL_CREATE = new enAclAdmin("ACL_CREATE");;
	public static final enAclAdmin ACL_UPDATE = new enAclAdmin("ACL_UPDATE");;
	public static final enAclAdmin ACL_READ = new enAclAdmin("ACL_READ");;
	public static final enAclAdmin ACL_DENY = new enAclAdmin("ACL_DENY");;

	
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
