package org.ebaloo.itkeeps.tools;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectionsFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ReflectionsFactory.class);

	private static final Map<String, Reflections> map = new HashMap<>();
	

	public static Reflections getReflections(String packagename) {

		if(!map.containsKey(packagename)) {
			
			logger.info("Add: " + packagename);
			
			map.put(packagename, new Reflections(packagename));
		}
		
		return map.get(packagename);
	}
	
	public static Reflections getReflections(Class<?> clasz) {
		return getReflections(clasz.getPackage().getName());
	}
	

}
