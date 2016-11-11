package org.ebaloo.itkeeps.tools;

import java.util.ArrayList;
import java.util.Arrays;

public class SecurityRole {

	public static final String GUEST = "GUEST";
	public static final String USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String ROOT = "ROOT";

	private static final ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[]{GUEST, USER, ADMIN, ROOT}));
	
	public final static String[] values() {
		return list.toArray(new String[list.size()]);
	}
	
	public final static String valueOf(String arg0) {
		if(!list.contains(arg0)) 
			throw new RuntimeException("TODO"); //TODO
		
		return arg0;
	}
	
}