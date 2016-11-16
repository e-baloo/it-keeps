package org.ebaloo.itkeeps.core.domain;


import java.util.function.Predicate;

import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.core.domain.vertex.BaseAbstract;

public class BaseUtils {
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static class WhereClause {
		
		public static String WHERE_CLAUSE__TRUE = " (true) ";
		public static String WHERE_CLAUSE__ENABLE_IS_TRUE = " (" + JBase.ENABLE + "=true) ";
		
		/*
		public static final String enable() {
			return WHERE_CLAUSE__ENABLE_IS_TRUE;
		}
		
		public static final void enable(final StringBuilder sb) {
			sb.append(enable());
		}

		public static final Predicate<? super BaseAbstract> enableFilter() {
			return e -> e.getProperty(JBase.ENABLE) != null && (boolean)e.getProperty(JBase.ENABLE);
		}
		*/
		//-------------------
		
		public final static String classIsntanceOf(final String targetClass, final boolean instanceOf) {
			return String.format("(@class %s '%s')", instanceOf ? "INSTANCEOF" : "=", targetClass);
		}
		
		public final static String classIsntanceOf(final Class<? extends BaseAbstract> targetClass, final boolean instanceOf) {
			return classIsntanceOf(targetClass.getSimpleName(), instanceOf);
		}
		
		public final static String classIsntanceOf(final ModelFactory.ModelClass<? extends BaseAbstract> targetClass, final boolean instanceOf) {
			return classIsntanceOf(targetClass.getClassName(), instanceOf);
		}

		public final static void classIsntanceOf(final Class<? extends BaseAbstract> targetClass, final boolean instanceOf, final StringBuilder sb) {
			sb.append(classIsntanceOf(targetClass, instanceOf));
		}

		public final static void classIsntanceOf(final ModelFactory.ModelClass<? extends BaseAbstract> targetClass, final boolean instanceOf, final StringBuilder sb) {
			sb.append(classIsntanceOf(targetClass, instanceOf));
		}

		/*
		public static final Predicate<? super BaseAbstract> classInstanceOf(final Class<? extends BaseAbstract> targetClass, final boolean instanceOf) {
			return classInstanceOf(targetClass.getSimpleName(), instanceOf);
		}
		*/

		/*
		public static final Predicate<? super BaseAbstract> classInstanceOf(final String targetClass, final boolean instanceOf) {
			if(instanceOf) {
				//targetClass.isInstance(baseAbstract) si targetClass => Class<? extends BaseAbstract>
				return orientVertex -> orientVertex.getOrientVertex().getType().getName().equals(targetClass) || orientVertex.getOrientVertex().getType().getAllSuperClasses().stream().anyMatch(e -> e.getName().equals(targetClass));
			} else {
				return orientVertex -> orientVertex.getOrientVertex().getType().getName().equals(targetClass);
			}
		}
		*/
		
		
		
	}


}
