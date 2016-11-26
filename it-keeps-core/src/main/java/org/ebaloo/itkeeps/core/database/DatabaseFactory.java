
package org.ebaloo.itkeeps.core.database;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.eRelation;
import org.ebaloo.itkeeps.core.domain.vertex.vAclAdmin;
import org.ebaloo.itkeeps.core.domain.vertex.vAclData;
import org.ebaloo.itkeeps.core.domain.vertex.vAclEnum;
import org.ebaloo.itkeeps.core.domain.vertex.vAclOwner;
import org.ebaloo.itkeeps.core.domain.vertex.vAclRole;
import org.ebaloo.itkeeps.core.domain.vertex.vAuthentication;
import org.ebaloo.itkeeps.core.domain.vertex.vBase;
import org.ebaloo.itkeeps.core.tools.ReflectionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;


public class DatabaseFactory {

	private static Logger logger = LoggerFactory.getLogger(DatabaseFactory.class);
	
	private static boolean init = false;
	
	public static void init() {

		if (init)
			return;
			
			OrientGraphNoTx graph = (OrientGraphNoTx) GraphFactory.getOrientGraphNoTx();

			logger.info("Start init database schema!");
			graph.commit();

			IninDatabase.initEdge(graph);

			logger.info(" - Edge schemat init done.");

			IninDatabase.initVertex(graph);
			graph.commit();

			logger.info(" - Vertrex schemat init done.");
			
			
			GraphFactory.executeNoReturn(null, "ALTER DATABASE DATETIMEFORMAT \"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\"");
			
			try {
				GraphFactory.executeNoReturn(null, "CREATE INDEX " + vAclEnum.class.getSimpleName() + "." + vAclEnum.NAME + " NOTUNIQUE_HASH_INDEX");
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}

			
			vAuthentication.init(enAuthentication.class, vAuthentication.class);
			logger.info(" - init Authentication done.");

			vAclData.init(enAclData.class, vAclData.class);
			logger.info(" - init AclData done.");

			vAclAdmin.init(enAclAdmin.class, vAclAdmin.class);
			logger.info(" - init AclAdmins done.");

			vAclOwner.init(enAclOwner.class, vAclOwner.class);
			logger.info(" - init AclOwner done.");

			vAclRole.init(enAclRole.class, vAclRole.class);
			logger.info(" - init AclSecurityRole done.");


			
			
			init = true;
	}
	
	
	private static class IninDatabase {

		private static Logger logger = LoggerFactory.getLogger(IninDatabase.class);

		private static String getAllSuperclass(Class<?> c) {
			String value = c.getSimpleName();
			
			if (c.getSuperclass() == null) {
				return value;
			}
			
			/*
			if(!c.isAnnotationPresent(DatabaseVertrex.class)) {
				return getAllSuperclass(c.getSuperclass());
			}
			*/
			
			return getAllSuperclass(c.getSuperclass()) + "." + value;
		}

		private static void initVertex(OrientBaseGraph graph) {

			Map<String, Class<?>> map = new TreeMap<String, Class<?>>();

			for (Class<?> clasz : ReflectionsFactory.getReflections(vBase.class).getTypesAnnotatedWith(DatabaseVertrex.class)) {
				if (clasz != null && clasz.getSuperclass() != null) {
					map.put(getAllSuperclass(clasz), (Class<?>) clasz);
				}
			}

			for (Entry<String, Class<?>> es : map.entrySet()) {
				addVertrexType(graph, es.getValue());
			}
		}

		public static void initEdge(OrientBaseGraph graph) {

			Map<String, Class<?>> map = new TreeMap<String, Class<?>>();

			for (Class<?> clasz : ReflectionsFactory.getReflections(eRelation.class).getTypesAnnotatedWith(DatabaseEdge.class)) {
				
				logger.info(clasz.getName());
				
				if (clasz.getSuperclass() != null) {
					map.put(getAllSuperclass(clasz), (Class<?>) clasz);
				}
			}

			for (Entry<String, Class<?>> es : map.entrySet()) {
				addEdgeType(graph, es.getValue());
			}
		}

		private static void addEdgeType(OrientBaseGraph graph, Class<?> clasz) {

			if (clasz.isAnnotationPresent(DatabaseEdge.class)) {

				//DatabaseEdge classAnotation = clasz.getAnnotation(DatabaseEdge.class);

				OrientEdgeType oClass = graph.getEdgeType(clasz.getSimpleName());

				OrientEdgeType oClassParent = graph.getEdgeType(clasz.getSuperclass().getSimpleName());
				if (clasz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName())) {
					oClassParent = graph.getEdgeBaseType();
				}

				if (oClass == null) {
					logger.warn("Add Edge @" + clasz.getSimpleName() + " child of @" + oClassParent);
					oClass = graph.createEdgeType(clasz.getSimpleName(), oClassParent.getName());
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Edge @" + clasz.getSimpleName() + " exist, child of @" + oClassParent);
				}

				/*
				if (oClass.isAbstract() != classAnotation.isAbstract()) {
					oClass.setAbstract(classAnotation.isAbstract());
					logger.warn("Update Edge @" + oClass + " set isAbstract at " + classAnotation.isAbstract());
				}
				*/

			}

		}

		public static void addVertrexType(OrientBaseGraph graph, Class<?> clasz) {

			if (clasz.isAnnotationPresent(DatabaseVertrex.class)) {
				
				
				Map<String, DatabaseProperty> mapSchemaProperty = new HashMap<String, DatabaseProperty>();
				//mapSchema.put(clasz.getSimpleName(), mapSchemaProperty);

				DatabaseVertrex classAnotation = clasz.getAnnotation(DatabaseVertrex.class);

				String className = clasz.getSimpleName();
				if(StringUtils.isNotBlank(classAnotation.name())) {
					className = classAnotation.name();
				}
				
				
				
				OrientVertexType oClass = graph.getVertexType(className);

				Class<?> parentClass = clasz.getSuperclass();				
				while(!parentClass.isAnnotationPresent(DatabaseVertrex.class) && (parentClass.getSuperclass() != null)) {
					parentClass = parentClass.getSuperclass();				
				}
				
				OrientVertexType oParentClass = graph.getVertexType(parentClass.getSimpleName());
				if (parentClass.getSimpleName().equals(Object.class.getSimpleName())) {
					oParentClass = graph.getVertexBaseType();
				}

				if (oClass == null) {
					logger.warn("Add Vertrex @" + className + " child of @" + oParentClass);
					oClass = graph.createVertexType(className, oParentClass.getName());
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Vertrex @" + className + " exist, child of @" + oParentClass);
				}

				/*
				if (oClass.isAbstract() != classAnotation.isAbstract()) {
					oClass.setAbstract(classAnotation.isAbstract());
					logger.warn("Update Vertrex @" + oClass + " set isAbstract at " + classAnotation.isAbstract());
				}
				*/	

				ArrayList<DatabaseProperty> listDatabaseProperty = new ArrayList<DatabaseProperty>();

				/*
				for (Field field : clasz.getFields()) {
					if (field.isAnnotationPresent(DatabaseProperty.class)) {
						listDatabaseProperty.add(field.getAnnotation(DatabaseProperty.class));
					}
				}
				*/

				for (Method method : clasz.getMethods()) {
					if (method.isAnnotationPresent(DatabaseProperty.class)) {
						listDatabaseProperty.add(method.getAnnotation(DatabaseProperty.class));
					}
				}

				for (DatabaseProperty propertyAnnotation : listDatabaseProperty) {

					OProperty oProperty = oClass.getProperty(propertyAnnotation.name());
					
					mapSchemaProperty.put(propertyAnnotation.name(), propertyAnnotation);
					

					if (oProperty == null) {
						oProperty = oClass.createProperty(propertyAnnotation.name(), propertyAnnotation.type());
						logger.warn("Add property Vertrex @" + oClass + "." + propertyAnnotation.name());
					} else {
						if (logger.isDebugEnabled())
							logger.debug("Proprety @" + oClass + "." + propertyAnnotation.name() + " exist.");

						if (oProperty.getType() != propertyAnnotation.type()) {
							logger.warn("Change type for Vertrex @" + oClass + "." + propertyAnnotation.name()
									+ " from " + oProperty.getType() + "to " + propertyAnnotation.type());
							oProperty.setType(propertyAnnotation.type());
						}
					}

					if (oProperty.isNotNull() != propertyAnnotation.isNotNull()) {
						oProperty.setNotNull(propertyAnnotation.isNotNull());
						logger.warn("Set defnotNull for Vertrex @" + oClass + "." + propertyAnnotation.name() + " to "
								+ propertyAnnotation.isNotNull());
					}

					if (oProperty.isReadonly() != propertyAnnotation.isReadOnly()) {
						oProperty.setReadonly(propertyAnnotation.isReadOnly());
						logger.warn("Set readOnly for Vertrex @" + oClass + "." + propertyAnnotation.name() + " to "
								+ propertyAnnotation.isReadOnly());
					}

// ITA-198
//					if (StringUtils.isNotBlank(propertyAnnotation.defaultValue())) {
//						if (!propertyAnnotation.defaultValue().equals(oProperty.getDefaultValue())) {
//							oProperty.setDefaultValue(propertyAnnotation.defaultValue());
//							logger.warn("Set defaultValue for Vertrex @" + oClass + "." + propertyAnnotation.name()
//									+ " to " + propertyAnnotation.defaultValue());
//						}
//					}

					/*
					 * if(propertyAnnotation.isIndex()) {
					 * oProperty.createIndex("IDX_" +
					 * propertyAnnotation.name().toUpperCase()); }
					 */
				}

			}
		}

	}
	
	
}
