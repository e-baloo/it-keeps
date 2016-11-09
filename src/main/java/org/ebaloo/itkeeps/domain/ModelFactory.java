
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

	//public final static ClassHierarchy classHierarchy = new ClassHierarchy();

	@SuppressWarnings("unchecked")
	public static void init() {

		if (init)
			return;

			logger.info("Start init model!");

			for (Class<?> clasz : ModelFactory.getReflections().getTypesAnnotatedWith(ModelClassAnnotation.class)) {

				if(clasz.getAnnotation(ModelClassAnnotation.class) != null) {
					
					if (logger.isDebugEnabled())
						logger.debug("Add @" + clasz.getSimpleName());

					ModelClass<BaseAbstract> modelClass = new ModelClass<BaseAbstract>(BaseAbstract.class.getClass().cast(clasz));
					
					mapIBaseClass.put(clasz.getSimpleName(), modelClass);
					//classHierarchy.add(clasz.getPackage().getName().substring(Base.class.getPackage().getName().length()), modelClass);
				}
			}

			logger.info(" - Edge schemat init done.");

			init = true;
	}

	public static Reflections getReflections() {
		
		return ReflectionsFactory.getReflections(Base.class.getPackage().getName());

	}

	private static Map<String, ModelClass<BaseAbstract>> mapIBaseClass = new HashMap<>();

	/*
	public static class ClassHierarchy implements I18nInterface{
        public final String name;
        public final ClassHierarchy parent;
		public final Map<String, ClassHierarchy> packages = new LinkedHashMap<>();
		public final List<ModelClass<?>> classes = new ArrayList<>();
		private final I18nFactory.I18nComponent i18nComponent;

        ClassHierarchy() {
            this(null, "");
        }

        ClassHierarchy(ClassHierarchy parent, String name) {
            this.parent = parent;
            this.name = name;
			this.i18nComponent = I18nFactory.getComponent("@package", "@" + this.getFullPackage());
        }

        public boolean isRoot() {
            return this.parent == null;
        }

        public String getFullPackage() {
            return (!this.isRoot() && !this.parent.isRoot() ? this.parent.getFullPackage() + "." : "") + this.name;
        }

		public Set<Map.Entry<String, ModelClass<?>>> getAll() {
            Set<Map.Entry<String, ModelClass<?>>> result = new HashSet<>();
			packages.values().forEach(e -> result.addAll(e.getAll()));
			classes.forEach(e -> result.add(new AbstractMap.SimpleEntry<>(this.getFullPackage(), e)));
            return result;
		}

		public void add(String packageName, ModelClass<?> modelClass) {
			if(packageName.startsWith("."))
				packageName = packageName.substring(1);

			if(StringUtils.isBlank(packageName)) {
				classes.add(modelClass);
			} else {
				String pack = StringUtils.split(packageName, ".")[0];
				if (!packages.containsKey(pack)) {
				    packages.put(pack, new ClassHierarchy(this, pack));
				}
				packages.get(pack).add(packageName.substring(pack.length()), modelClass);
			}
		}

        public Stream<Map.Entry<String, ModelClass<?>>> stream() {
            return this.getAll().stream();
        }

        public static Collector<? super Map.Entry<String, ModelClass<?>>, ?, ClassHierarchy> toClassHierarchy() {
            return Collector.of(
                ClassHierarchy::new,
                (a, t) -> a.add(t.getKey(), t.getValue()),
                (left, right) -> {
                    right.getAll().forEach(e -> left.add(e.getKey(), e.getValue()));
                    return left;
                });
        }

        public ClassHierarchy sortTree(Comparator<? super Map.Entry<String, ClassHierarchy>> packageComparator,
                                       Comparator<? super ModelClass<?>> classComparator) {
            Map<String, ClassHierarchy> hashMap = new LinkedHashMap<>();
            packages
                .entrySet()
                .stream()
                .sorted(packageComparator)
                .forEach(e -> {
                    hashMap.put(e.getKey(), e.getValue());
                    e.getValue().sortTree(packageComparator, classComparator);
                });
            this.packages.clear();
            this.packages.putAll(hashMap);
            classes.sort(classComparator);
            return this;
        }

		@Override
		public I18nFactory.I18nComponent getI18n() {
			return this.i18nComponent;
		}
	}
	*/

	/*
	 * 
	 * 
	 */

	/*
	public static class ModelParameter implements I18nInterface {

		@SuppressWarnings("unused")
		private final ModelClass<?> modelClass;
		private final Parameter parameter;
		private final ModelParameterAnnotation annotation;
		private final I18nFactory.I18nComponent i18nComponent;

		protected ModelParameter(final ModelClass<?> modelClass, final Parameter parameter) {
			this.modelClass = modelClass;
			this.parameter = parameter;
			this.annotation = parameter.getAnnotation(ModelParameterAnnotation.class);
			if(parameter.isAnnotationPresent(I18nAnnotation.class)) {
				i18nComponent = I18nFactory.getComponent(parameter.getAnnotation(I18nAnnotation.class), modelClass.getI18n().getBundle(), this.parameter.getName());
			} else {
				logger.error("I18nAnnotation not found. Class: " + modelClass.getClassName() + ". Parameter: " + this.parameter.getName());
				i18nComponent = I18nFactory.getComponent(modelClass.getI18n().getBundle(), this.parameter.getName());
			}
		}

		public boolean isInstanceOf() {
			return this.annotation == null || this.annotation.instanceOf();
		}

		public Class<?> getType() {
			if(getAnnotation() != null) {
				if(BaseAbstract.class.isAssignableFrom(getAnnotation().type())) {
					return ModelFactory.get(getAnnotation().type().getSimpleName()).getClasz();
				}
			}
			return this.parameter.getType();
		}

		private ModelParameterAnnotation modelParameterAnnotation = null;

		private ModelParameterAnnotation getAnnotation() {
			if (modelParameterAnnotation == null) {
				if (parameter.isAnnotationPresent(ModelParameterAnnotation.class)) {
					modelParameterAnnotation = parameter.getAnnotation(ModelParameterAnnotation.class);
				}
			}

			return modelParameterAnnotation;
		}

		@Override
		public I18nFactory.I18nComponent getI18n() {
			return i18nComponent;
		}

	}*/

	/*
	 * 
	 * 
	 * 
	 */

	/*
	public static class ModelProperty implements I18nInterface {
		private final String propertyName;
		// private final String className;
		private final ModelClass<?> modelClass;

		private Method methodGet = null;
		private Method methodSet = null;
		private Method methodAdd = null;
		private Method methodRemove = null;
		private Method methodClean = null;
		private Method methodAction = null;

		private ClassifiedType classifiedType = ClassifiedType.OPTIONAL;

		private AuthAccessLevel accessLevel = AuthAccessLevel.NULL;

		private boolean isInstanceOf = true;

		private I18nFactory.I18nComponent i18nComponent;

		protected ModelProperty(final ModelClass<?> modelClass, final String propertyName) {
			this.propertyName = propertyName;
			this.modelClass = modelClass;
		}

		public boolean isInstanceOf() {
			return isInstanceOf;
		}

		public boolean canGet(BaseAbstract ibase) {
			return this.canGet(ibase.getContext()); // && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.READ);
		}

		public boolean canGet(UserContextInterface context) {
			return ((methodGet != null) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.READ)));
		}

		public boolean canSet(BaseAbstract ibase) {
			return canSet(ibase.getContext()) && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.UPDATE);
		}

		public boolean canSet(UserContextInterface context) {
			return ((methodSet != null) && (canGet(context)) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.UPDATE)));
		}

		//TODO: n'a pas le role de tester le scope en theorie
		public boolean canAdd(BaseAbstract ibase) {
			return canAdd(ibase.getContext()) && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.UPDATE);
		}

		public boolean canAdd(UserContextInterface context) {
			return ((methodAdd != null) && (canGet(context)) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.UPDATE)));
		}

		//TODO: n'a pas le role de tester le scope en theorie
		public boolean canRemove(BaseAbstract ibase) {
			return canRemove(ibase.getContext()) && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.UPDATE);
		}

		public boolean canRemove(UserContextInterface context) {
			return ((methodRemove != null) && (canGet(context)) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.UPDATE)));
		}

		//TODO: n'a pas le role de tester le scope en theorie
		public boolean canClean(BaseAbstract ibase) {
			return canClean(ibase.getContext()) && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.UPDATE);
		}

		public boolean canClean(UserContextInterface context) {
			return ((methodClean != null) && (canGet(context)) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.UPDATE)));
		}

		//TODO: n'a pas le role de tester le scope en theorie
		public boolean canAction(BaseAbstract ibase) {
			return canAction(ibase.getContext()) && ibase.getContext().testScopeAccessLevel(ibase, AuthAccessLevel.UPDATE);
		}

		public boolean canAction(UserContextInterface context) {
			return ((methodAction != null) && (canGet(context)) && (context.testRightAccessLevel(getClassName(), getPropertyName(), AuthAccessLevel.UPDATE)));
		}

		public boolean canEdit(BaseAbstract ibase) {
			return canEdit(ibase.getContext());
		}

		public boolean canEdit(UserContextInterface context) {
			return canSet(context) || canAdd(context) || canRemove(context) || canAction(context);
		}

		
		public boolean canEditWithoutRight() {
			return (methodGet != null) && ((methodAdd != null) || (methodRemove != null) || (methodSet != null)); 
		}

		public final String getPropertyName() {
			return this.propertyName;
		}

		
		public Object callGet(BaseAbstract ibase) {
			if (!this.canGet(ibase) && !this.propertyName.equals(Base.GUID)) {
				return null;
			}

			try {
				return this.methodGet.invoke(ibase);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}

		

		public boolean callSet(BaseAbstract ibase, String arg) {
			if (!this.canSet(ibase))
				return false;

			try {
				this.methodSet.invoke(ibase, arg);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(ibase + " / " + ibase.getContext() + " / " + arg);
				return false;
			}
		}


		public boolean callAdd(BaseAbstract ibase, String arg) {
			if (!this.canAdd(ibase))
				return false;

			try {
				this.methodAdd.invoke(ibase, arg);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(ibase + " / " + ibase.getContext() + " / " + arg);
				return false;
			}
		}



		
		public boolean callRemove(BaseAbstract ibase, String arg) {
			if (!this.canRemove(ibase))
				return false;

			try {
				this.methodRemove.invoke(ibase, arg);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		


		
		public boolean callClean(BaseAbstract ibase) {
			if (!this.canClean(ibase))
				return false;

			try {
				this.methodClean.invoke(ibase);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}



		
		public boolean callAction(BaseAbstract ibase) {
			
			if (!this.canAction(ibase))
				return false;

			try {
				return (boolean) this.methodAction.invoke(ibase);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return false;
			}
		}


		public Class<?> getValueType(BaseAbstract obj) {
			
			if(this.propertyName.equals(Base.GUID)) {
				return this.methodGet.getReturnType();
			}
			
			if (!this.canGet(obj)) {
				return null;
			}

			return this.methodGet.getReturnType();
		}

		public boolean isValueIterator(BaseAbstract obj) {
			return Iterable.class.isAssignableFrom(this.getValueType(obj));
		}

		private Class<?> iteratorType = null;
		@SuppressWarnings("restriction")
		public Class<?> getValueIteratorType(BaseAbstract obj) {

			if (this.iteratorType != null) {
				return iteratorType;
			}

			if (!this.isValueIterator(obj)) {
				return null;
			}

			Type returnType = this.methodGet.getGenericReturnType();

			if (returnType instanceof ParameterizedType) {

				ParameterizedType paramType = (ParameterizedType) returnType;
				Type[] argTypes = paramType.getActualTypeArguments();

				if (argTypes.length > 0) {

					iteratorType = argTypes[0] instanceof WildcardTypeImpl ? 
							(Class<?>) ((WildcardTypeImpl) argTypes[0]).getUpperBounds()[0] : 
							((Class<?>) argTypes[0]);

				}
			}

			return iteratorType;
		}

		private void setI18n(I18nFactory.I18nComponent i18nComponent) {
			this.i18nComponent = i18nComponent;
		}

		@Override
		public I18nFactory.I18nComponent getI18n() {
			if(i18nComponent == null) {
				logger.error("Property: i18n not defined. Class:" + this.getClassName() + ". Method:" + this.getPropertyName());
				i18nComponent = I18nFactory.getComponent(this.getModelClass().getI18n().getBundle(), this.getPropertyName());
			}
			return i18nComponent;
		}

		public String getClassName() {
			return this.getModelClass().getClassName();
		}

		public ClassifiedType getClassifiedType() {
			return classifiedType;
		}

		protected void setClassifiedType(ClassifiedType classifiedType) {
			if (this.classifiedType.ordinal() > classifiedType.ordinal()) {
				this.classifiedType = classifiedType;
			}
		}



		public ModelClass<?> getModelClass() {
			return modelClass;
		}

		public AuthAccessLevel getAccessLevel() {
			return accessLevel;
		}

		protected void setAccessLevel(AuthAccessLevel newAuthAccessLevel) {
			this.accessLevel = AuthAccessLevel.max(accessLevel, newAuthAccessLevel);
		}
	}
	*/

	public static class ModelClass<T extends BaseAbstract> {

		private final String className;
		private final Class<T> clasz;
		private final ModelClassAnnotation modelClassAnnotation;

		//private HashMap<String, ModelProperty> mapProperty = new HashMap<String, ModelProperty>();

		protected ModelClass(Class<T> clasz) {

			this.clasz = clasz;
			this.className = clasz.getSimpleName();
			this.modelClassAnnotation = this.clasz.getAnnotation(ModelClassAnnotation.class);

			
			if(StringUtils.isNoneBlank(this.modelClassAnnotation.name()) && !this.className.equals(modelClassAnnotation.name())) {
				throw new RuntimeException("The Class name [" + this.className +  "] is not the same name with the Annotation name [" + modelClassAnnotation.name() + "]!");
			}
			
			

			//this.setProperties(clasz);

			/*
			for (String disableProperty : modelClassAnnotation.disableProperty()) {
				mapProperty.remove(disableProperty);
				logger.info("Remove property from " + this.getClassName() + "." + disableProperty);
			}*/

		}

//		private Constructor<?> defaultConstructor = null; //TODO A typer

//		private Constructor<?> constructor = null; //TODO A typer
//		private ArrayList<ModelParameter> constructorParameters = new ArrayList<ModelParameter>();

		/*
		public List<ModelParameter> getConstructorParameters(UserContextInterface context) {
			
			if(!this.canConstructor(context)) {
				return null;
			}
			
			return new ArrayList<ModelParameter>(this.constructorParameters);
		}

		public boolean canConstructor(UserContextInterface context) {
			return ((this.constructor != null) && (!this.modelClassAnnotation.isAbstract()) && (context.testRightAccessLevel(className, "", AuthAccessLevel.CREATE)));
		}

		public boolean canConstructorScope(BaseAbstract obj) {
			return canConstructor(obj.getContext()) && (obj.getScope().isEmpty() || obj.getContext().getAllScopeFromUser().stream().anyMatch(e -> obj.getScope().contains(e)));
		}

		public BaseAbstract callConstructor(UserContextInterface context, Object... args) {
			if (!this.canConstructor(context)) {
				return null;
			}

			try {
				
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(context);
				list.addAll(Arrays.asList(args));
				
				return (BaseAbstract) this.constructor.newInstance(list.toArray());
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| InstantiationException e) {

				// TODO
				logger.error("MESSAGE TODO", e);
				return null;
			}
		}

		public BaseAbstract callDefaultConstructor(UserContextInterface context, String name) {
			if (!this.canConstructor(context)) {
				return null;
			}

			try {
				
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(context);
				list.add(name);
				
				return (BaseAbstract) this.defaultConstructor.newInstance(list.toArray());
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| InstantiationException e) {

				// TODO
				logger.error("MESSAGE TODO", e);
				return null;
			}
		}

		private void setConstructor(Class<?> clasz) {

			ArrayList<Constructor<?>> constructorList = new ArrayList<Constructor<?>>();

			for (Constructor<?> constructor : clasz.getConstructors()) {
				if (constructor.isAnnotationPresent(ModelConstructorAnnotation.class)) {
					constructorList.add(constructor);
				}
			}

			if (constructorList.size() == 0) {
				return;
			}

			if (constructorList.size() > 1) {
				throw new RuntimeException(new Exception("To many construcotor font for @" + clasz.getSimpleName()));
			}

			this.constructor = constructorList.get(0);
			
			checkParameterConstructor(this.constructor);

			this.constructorParameters = new ArrayList<ModelParameter>();
			
			for (int i = 1 ; i < this.constructor.getParameters().length; i++) {
				
				Parameter parameter  = this.constructor.getParameters()[i];

				constructorParameters.add(new ModelParameter(this, parameter));
			}
				
		}

		private void setDefaultConstructor(Class<?> clasz) {

			ArrayList<Constructor<?>> constructorList = new ArrayList<Constructor<?>>();

			for (Constructor<?> constructor : clasz.getConstructors()) {
				if (constructor.isAnnotationPresent(ModelDefaultConstructorAnnotation.class)) {
					constructorList.add(constructor);
				}
			}

			if (constructorList.size() == 0) {
				throw new RuntimeException("One '@ModelDefaultConstructorAnnotation' is requide for @" + clasz.getSimpleName());
			}

			if (constructorList.size() > 1) {
				throw new RuntimeException("To many '@ModelDefaultConstructorAnnotation' construcotor for @" + clasz.getSimpleName());
			}

			this.defaultConstructor = constructorList.get(0);
			
			this.checkParameterDefaultConstructor(this.defaultConstructor);

		}

		private void setAccessLevel(AuthAccessLevel authAccessLevel) {
			this.accessLevel = AuthAccessLevel.max(accessLevel, authAccessLevel);
		} */

		public String getClassName() {
			return this.clasz.getSimpleName();
		}

		public Class<T> getClasz() {
			return this.clasz;
		}

		public T newInstance() throws IllegalAccessException, InstantiationException {
			return this.clasz.newInstance();
		}

		/*
		private void setProperties(Class<?> clasz) {

			for (Method method : clasz.getMethods()) {

				if (method.isAnnotationPresent(ModelPropertyAnnotation.class)) {

					ModelPropertyAnnotation modelPropertyAnnotation = method
							.getAnnotation(ModelPropertyAnnotation.class);

					// Creation de la propriété si inexistante //
					if (!mapProperty.containsKey(modelPropertyAnnotation.name())) {

						if (logger.isTraceEnabled())
							logger.trace("Add @" + clasz.getSimpleName() + "." + modelPropertyAnnotation.name());

						mapProperty.put(modelPropertyAnnotation.name(),
								new ModelProperty(this, modelPropertyAnnotation.name()));

						// logger.info("TEST @" + clasz.getSimpleName() + "." +
						// mp.name() +" : " + method);
					}

					ModelProperty modelProperty = mapProperty.get(modelPropertyAnnotation.name());

					// Attribution de la methode //
					if (logger.isTraceEnabled())
						logger.trace("Set @" + clasz.getSimpleName() + "." + modelPropertyAnnotation.name() + " : "
								+ modelPropertyAnnotation.type());

					switch (modelPropertyAnnotation.type()) {
					case GET:
						checkParameterTypesGetClean(clasz, method);
						modelProperty.methodGet = method;
						modelProperty.isInstanceOf = modelPropertyAnnotation.instanceOf();
						break;
					case SET:
						checkParameterTypesSetAddRemove(clasz, method);
						modelProperty.methodSet = method;
						break;

					case ADD:
						checkParameterTypesSetAddRemove(clasz, method);
						modelProperty.methodAdd = method;
						break;

					case REMOVE:
						checkParameterTypesSetAddRemove(clasz, method);
						modelProperty.methodRemove = method;
						break;

					case ACTION:
						checkParameterTypesAction(clasz, method);
						modelProperty.methodAction = method;
						break;

					case CLEAN:
						checkParameterTypesGetClean(clasz, method);
						modelProperty.methodClean = method;
						break;

					default:
						break;
					}

					if(method.isAnnotationPresent(I18nAnnotation.class)) {
						modelProperty.setI18n(I18nFactory.getComponent(method.getAnnotation(I18nAnnotation.class),
								this.getI18n().getBundle(), modelProperty.getPropertyName()));
					}

					modelProperty.setClassifiedType(modelPropertyAnnotation.classifiedType());
					modelProperty.setAccessLevel(modelPropertyAnnotation.accessLevel());

				}
			}

		}

		private void checkParameterTypesGetClean(Class<?> clasz, Method method) {
			Class<?>[] parameterTypes = method.getParameterTypes();

			if (parameterTypes.length != 0) {
				throw new RuntimeException(new Exception("methode @" + this.getClassName() + "." + method.getName()
						+ " have not good parameter (" + parameterTypes.length + ")"));
			}

		}

		private void checkParameterTypesAction(Class<?> clasz, Method method) {
			Class<?>[] parameterTypes = method.getParameterTypes();

			if (parameterTypes.length != 0) {
				throw new RuntimeException( new Exception("methode @" + clasz.getSimpleName() + "." + method.getName()
						+ " have not good parameter (" + parameterTypes.length + ")"));
			}
		}
		
		private void checkParameterConstructor(final Constructor<?> constructor) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();

			if (parameterTypes.length < 2) {
				throw new RuntimeException( new Exception("constructor @" + this.getClassName() + "." + constructor.getName()
						+ " have not good parameter (" + parameterTypes.length + ")"));
			}

			if (!parameterTypes[0].isAssignableFrom(UserContextInterface.class)) {
				throw new RuntimeException( new Exception("constructor @" + this.getClassName() + "." + constructor.getName()
						+ " 1 parameter is not @IUserContext (" + parameterTypes[0].getTypeName() + ")"));
			}

		}

		private void checkParameterDefaultConstructor(final Constructor<?> constructor) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();

			if (parameterTypes.length != 2) {
				throw new RuntimeException("constructor @" + this.getClassName() + "." + constructor.getName()
						+ " have not good parameter (" + parameterTypes.length + "), 2 parmater is redied");
			}

			if (!parameterTypes[0].isAssignableFrom(UserContextInterface.class)) {
				throw new RuntimeException("constructor @" + this.getClassName() + "." + constructor.getName()
						+ " 1 parameter is not @IUserContext (" + parameterTypes[0].getTypeName() + ")");
			}

			if (!parameterTypes[1].isAssignableFrom(String.class)) {
				throw new RuntimeException("constructor @" + this.getClassName() + "." + constructor.getName()
						+ " 2 parameter is not @String (" + parameterTypes[1].getTypeName() + ")");
			}

		}

		private void checkParameterTypesSetAddRemove(Class<?> clasz, Method method) {
			Class<?>[] parameterTypes = method.getParameterTypes();

			if (parameterTypes.length != 1) {
				throw new RuntimeException( new Exception("methode @" + clasz.getSimpleName() + "." + method.getName()
						+ " have not good parameter (" + parameterTypes.length + ")"));
			}

			if (!parameterTypes[0].isAssignableFrom(String.class)) {
				throw new RuntimeException( new Exception("methode @" + clasz.getSimpleName() + "." + method.getName()
						+ " 1 parameter is not @String (" + parameterTypes[0].getTypeName() + ")"));
			}
		}



		public List<String> getKeys() {
			List<String> list = new ArrayList<String>(mapProperty.keySet());
			Collections.sort(list);
			return list;
		}

		public ModelProperty get(String key) {
			return mapProperty.get(key);
		}

		public Set<Map.Entry<String, ModelProperty>> getProperties() {
			return mapProperty.entrySet();
		}

		@Override
		public I18nFactory.I18nComponent getI18n() {
			return i18nComponent;
			//return I18nFactory.getComponent(this.getI18nBundle(), this.getI18nLabel());
		}


		public AuthAccessLevel getAccessLevel() {
			return accessLevel;
		}
		*/
		
		public boolean isAbstract() {
			return this.modelClassAnnotation.isAbstract();
		}
		
		
		

		@SuppressWarnings({ "unchecked", "deprecation" })
		public final List<T> getAllBase(final boolean isInstanceof, final boolean selected) {
			return (List<T>) Base.getAllBase(this.clasz, isInstanceof, selected);
		}

		/*
		public final boolean canGraph() {
			return this.modelClassAnnotation.canGraph();
		}
		
		public final boolean canTrace() {
			return this.modelClassAnnotation.canTrace();
		}
		*/
	}


	/*
	public static List<String> getKeys() {

		if (!init) {
			init();
		}

		List<String> list = new ArrayList<>(mapIBaseClass.keySet());
		
		Collections.sort(list);
		
		return list;
	}
	*/

	/*
	public static Collection<? extends ModelClass<? extends BaseAbstract>> getClasses() {
		if(!init) {
			init();
		}
		return mapIBaseClass.values();
	}
*/
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseAbstract> ModelClass<T> get(Class<? extends T> clazz) {
		return (ModelClass<T>)get(clazz.getSimpleName());
	}

	public static ModelClass<BaseAbstract> get(String key) {

		if (!init) {
			init();
		}

		return mapIBaseClass.get(key);
	}

	/*
	public static void put(OrientBaseGraph graph) {

		Model.clear(graph);
		Map<String, Model> mapModel = new HashMap<String, Model>();

		// Create all Node
		for (Entry<String, Map<String, DatabaseProperty>> es : DatabaseFactory.getMapSchema().entrySet()) {
			mapModel.put(es.getKey(), new Model(graph, es.getKey()));
		}
		graph.commit();

		ArrayList<String> listModelClass = new ArrayList<String>(ModelFactory.getKeys());
		ArrayList<String> listDatabaseClass = new ArrayList<String>(DatabaseFactory.getMapSchema().keySet());

		for (String className : listDatabaseClass) {

			Model model = mapModel.get(className);

			if (model == null) {
				throw new Exception();
			}

			if (!listModelClass.contains(className)) {

				model.setUserShow(false);

				HashMap<String, String> value = new HashMap<String, String>();
				for (Entry<String, DatabaseProperty> databaseProperty : DatabaseFactory.getMapSchema().get(className)
						.entrySet()) {
					value.put(databaseProperty.getKey(), databaseProperty.getValue().type().toString());
				}
				model.setPropertys(value);

			} else {

				model.setUserShow(true);

				if (!ModelFactory.get(className).getAccessLevel().isRead()) {
					model.setUserShow(false);
				}

				ArrayList<String> listModelProperty = new ArrayList<String>(ModelFactory.get(className).getKeys());

				HashMap<String, String> value = new HashMap<String, String>();
				for (String propertyName : listModelProperty) {

					ModelProperty modelProperty = ModelFactory.get(className).get(propertyName);

					if (!modelProperty.getAccessLevel().isRead()) {
						continue;
					}

					if (DatabaseFactory.getMapSchema().get(className).containsKey(propertyName)) {

						value.put(propertyName, modelPropertyTypeToString(modelProperty));
					} else {

						Model destModel = null;

						if (modelProperty.isValueIterator()) {
							destModel = mapModel.get(modelProperty.getValueIteratorType().getSimpleName());
						} else {
							destModel = mapModel.get(modelProperty.getValueType().getSimpleName());
						}

						if (destModel != null) {
							model.setRelation(destModel);
						}
					}
				}

				model.setPropertys(value);
			}
		}

		graph.commit();
	}*/

	/*
	private static String modelPropertyTypeToString(ModelProperty modelProperty) {

		if (modelProperty == null)
			return "null";

		String value = modelProperty.getValueType().getSimpleName();

		if (modelProperty.isValueIterator()) {
			value += "<" + modelProperty.getValueIteratorType().getSimpleName() + ">";
		}

		return value.toUpperCase();

	}
*/
	// Check

	/*
	 * 
	 * TODO
	public static void check(UserContextInterface context) {

		OrientBaseGraph graph = GraphFactory.getOrientGraphNoTx();
		
		String cmdSQL = "";
		cmdSQL += "SELECT FROM " + Base.class.getSimpleName() + " ";
		cmdSQL += "WHERE " + Base.ENABLE + " = true ";

		Iterable<OrientVertex> iterable = graph.command(new OCommandSQL(cmdSQL)).execute();

		for (OrientVertex ov : iterable) {

			IBase ibase = ModelFactory.getBaseAbstract(context, guid).getStaticBaseAbstract(context, ov);

			check(ibase, context);
			graph.commit();

		}

	}
	*/


	public static BaseAbstract getBaseAbstract(final String guid) {
		return getBaseAbstract(new Guid(guid));
	}
	
	public static BaseAbstract getBaseAbstract(final Guid guid)  
	{
		return BaseAbstract.getBaseAbstract(guid); 

	}

	// Sorti non trié par rapport au guid //
	public static List<BaseAbstract> getBaseAbstract(final List<Guid> guid)
	{
		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName()
				+ " WHERE " + BaseUtils.WhereClause.enable()
				+ " AND (guid IN [" + guid.stream().map(e -> "'" + e.toString() + "'").collect(Collectors.joining(",")) + "])";

		return BaseAbstract.commandBaseAbstract(cmdSQL).stream()
				.filter(Objects::nonNull)
				.sorted((a,b) -> guid.indexOf(a.getGuid()) - guid.indexOf(b.getGuid()))
				.collect(Collectors.toList());
	}
	
}
