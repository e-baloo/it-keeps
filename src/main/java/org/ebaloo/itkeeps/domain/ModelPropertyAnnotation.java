/*
 * Copyright (c) 2001-2016 Group JCDecaux. 
 * 17 rue Soyer, 92523 Neuilly Cedex, France.
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information
 * of Group JCDecaux ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you
 * entered into with Group JCDecaux.
 */

package org.ebaloo.itkeeps.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelPropertyAnnotation {

	public enum TypeProperty {
		GET,
		SET,
		ADD,
		REMOVE, 
		CLEAN,
		ACTION, 
	}
	
	String name();
	TypeProperty type();
	
	boolean instanceOf() default true;
}
