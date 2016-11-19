package org.ebaloo.itkeeps.api;

import java.util.Collection;
import java.util.HashMap;

import org.ebaloo.itkeeps.api.annotation.EnumAbstract;

public class AclDataType extends EnumAbstract<String> {

	private static final HashMap<String, AclDataType> map = new HashMap<String, AclDataType>();

	public static final AclDataType CREATE = new AclDataType("CREATE", 0);
	public static final AclDataType UPDATE = new AclDataType("UPDATE", 1);
	public static final AclDataType READ = new AclDataType("READ", 2);
	public static final AclDataType DENY = new AclDataType("DENY", 3);

	private AclDataType(String name, int ordinal) {
		super(name, ordinal, name);
		map.put(name, this);
	}
	

	public static AclDataType valueOf(String name) {
		return map.get(name);
	};
	
	public static Collection<AclDataType> values() {
		return map.values();
	};
	
}
