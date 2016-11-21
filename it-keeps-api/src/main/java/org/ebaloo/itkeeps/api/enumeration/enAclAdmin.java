package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class enAclAdmin extends enAbstract<Integer> {

	private static final HashMap<String, enAclAdmin> map = new HashMap<String, enAclAdmin>();

	public static enAclAdmin DELEGATE =new enAclAdmin("DELEGATE", 0, 0x0001);
	public static enAclAdmin NO_DELEGATE = new enAclAdmin("NO_DELEGATE", 1, 0x0002);

	private enAclAdmin(String name, int ordinal, Integer value) {
		super(name, ordinal, value);
		map.put(name, this);
	}
	

	public static enAclAdmin valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<enAclAdmin> values() {
		return map.values();
	};
	
}
