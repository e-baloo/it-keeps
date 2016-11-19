package org.ebaloo.itkeeps.api.enumeration;

import java.util.Collection;
import java.util.HashMap;

public class AclAdminType extends EnumAbstract<Integer> {

	private static final HashMap<String, AclAdminType> map = new HashMap<String, AclAdminType>();

	public static AclAdminType DELEGATE =new AclAdminType("DELEGATE", 0, 0x0001);
	public static AclAdminType NO_DELEGATE = new AclAdminType("NO_DELEGATE", 1, 0x0002);

	private AclAdminType(String name, int ordinal, Integer value) {
		super(name, ordinal, value);
		map.put(name, this);
	}
	

	public static AclAdminType valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<AclAdminType> values() {
		return map.values();
	};
	
}
