
package org.ebaloo.itkeeps.domain;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.tools.ReflectionsFactory;
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

				mapIBaseClass.put(clasz.getSimpleName(), modelClass);
			}
		}

		logger.info(" - Edge schemat init done.");

		init = true;
	}

	public static Reflections getReflections() {

		return ReflectionsFactory.getReflections(Base.class.getPackage().getName());

	}

	private static Map<String, ModelClass<BaseAbstract>> mapIBaseClass = new HashMap<>();

	public static class ModelClass<T extends BaseAbstract> {

		private final String className;
		private final Class<T> clasz;
		private final ModelClassAnnotation modelClassAnnotation;

		protected ModelClass(Class<T> clasz) {

			this.clasz = clasz;
			this.className = clasz.getSimpleName();
			this.modelClassAnnotation = this.clasz.getAnnotation(ModelClassAnnotation.class);

			if (StringUtils.isNoneBlank(this.modelClassAnnotation.name())
					&& !this.className.equals(modelClassAnnotation.name()))
				throw new RuntimeException("The Class name [" + this.className
						+ "] is not the same name with the Annotation name [" + modelClassAnnotation.name() + "]!");
		}

		public String getClassName() {
			return this.clasz.getSimpleName();
		}

		public Class<T> getClasz() {
			return this.clasz;
		}

		public T newInstance() throws IllegalAccessException, InstantiationException {
			return this.clasz.newInstance();
		}

		public boolean isAbstract() {
			return this.modelClassAnnotation.isAbstract();
		}

		@SuppressWarnings({ "unchecked", "deprecation" })
		public final List<T> getAllBase(final boolean isInstanceof, final boolean selected) {
			return (List<T>) Base.getAllBase(this.clasz, isInstanceof, selected);
		}

		public boolean isInstance(BaseAbstract baseAbstract) {
			return this.clasz.isInstance(baseAbstract);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseAbstract> ModelClass<T> get(Class<? extends T> clazz) {
		return (ModelClass<T>) get(clazz.getSimpleName());
	}

	public static ModelClass<BaseAbstract> get(String key) {

		if (!init) {
			init();
		}

		return mapIBaseClass.get(key);
	}

	public static BaseAbstract getBaseAbstract(final String guid) {
		return getBaseAbstract(new Guid(guid));
	}

	public static BaseAbstract getBaseAbstract(final Guid guid) {
		return BaseAbstract.getBaseAbstract(guid);

	}

	// Sorti non trié par rapport au guid //
	public static List<BaseAbstract> getBaseAbstract(final List<Guid> guid) {
		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable()
				+ " AND (guid IN [" + guid.stream().map(e -> "'" + e.toString() + "'").collect(Collectors.joining(","))
				+ "])";

		return BaseAbstract.commandBaseAbstract(cmdSQL).stream().filter(Objects::nonNull)
				.sorted((a, b) -> guid.indexOf(a.getGuid()) - guid.indexOf(b.getGuid())).collect(Collectors.toList());
	}

}
