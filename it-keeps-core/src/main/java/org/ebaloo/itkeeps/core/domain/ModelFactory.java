
package org.ebaloo.itkeeps.core.domain;

import java.util.*;

import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract.ModelClass;
import org.ebaloo.itkeeps.core.tools.ReflectionsFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelFactory {

	private static Logger logger = LoggerFactory.getLogger(ModelFactory.class);

	private static boolean init = false;

	@SuppressWarnings("unchecked")
	public static void init() {

		if (init)
			return;

		logger.info("Start init model!");

		for (Class<?> clasz : ModelFactory.getReflections().getTypesAnnotatedWith(ModelClassAnnotation.class)) {

			if (clasz.getAnnotation(ModelClassAnnotation.class) != null) {

				if (logger.isDebugEnabled())
					logger.debug("Add @" + clasz.getSimpleName());

				ModelClass<BaseAbstract> modelClass = new ModelClass<BaseAbstract>(
						BaseAbstract.class.getClass().cast(clasz));

				mapName.put(clasz.getSimpleName(), modelClass);
				mapClass.put((Class<? extends BaseAbstract>) clasz, modelClass);
			}
		}

		logger.info(" - Edge schemat init done.");

		init = true;
	}

	public static Reflections getReflections() {

		return ReflectionsFactory.getReflections(ModelFactory.class.getPackage().getName());

	}

	private static Map<String, ModelClass<BaseAbstract>> mapName = new HashMap<>();
	private static Map<Class<? extends BaseAbstract>, ModelClass<BaseAbstract>> mapClass = new HashMap<>();



	@SuppressWarnings("unchecked")
	public static <T extends BaseAbstract> ModelClass<T> get(Class<? extends T> clazz) {

		if (!init)
			init();

		return (ModelClass<T>) mapClass.get(clazz);
	}

	public static ModelClass<BaseAbstract> get(String key) {

		if (!init)
			init();

		return mapName.get(key);
	}

	/*
	public static BaseAbstract getBaseAbstract(final String guid) {
		return getBaseAbstract(new Guid(guid));
	}

	public static BaseAbstract getBaseAbstract(final Guid guid) {
		return BaseAbstract.getBaseAbstract(guid);

	}
*/

	// Sorti non trié par rapport au guid //
	/*
	public static List<BaseAbstract> getBaseAbstract(final List<Guid> guid) {
		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable()
				+ " AND (guid IN [" + guid.stream().map(e -> "'" + e.toString() + "'").collect(Collectors.joining(","))
				+ "])";

		return BaseAbstract.commandBaseAbstract(cmdSQL).stream().filter(Objects::nonNull)
				.sorted((a, b) -> guid.indexOf(a.getGuid()) - guid.indexOf(b.getGuid())).collect(Collectors.toList());
	}
	*/

}
