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


package com.jcdecaux.itasset.model.domain.other.auth;

import java.util.List;

import com.jcdecaux.itasset.model.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcdecaux.itasset.CONST.CLASS;
import com.jcdecaux.itasset.common.RootUserContext;
import com.jcdecaux.itasset.common.annotation.I18nAnnotation;
import com.jcdecaux.itasset.common.api.UserContextInterface;
import com.jcdecaux.itasset.common.enumeration.AuthAccessLevel;
import com.jcdecaux.itasset.model.annotation.ModelPropertyAnnotation.TypeProperty;
import com.jcdecaux.itasset.model.annotation.database.DatabaseProperty;
import com.jcdecaux.itasset.model.annotation.database.DatabaseVertrex;
import com.jcdecaux.itasset.model.domain.BaseAbstract;
import com.jcdecaux.itasset.model.domain.CONST;
import com.jcdecaux.itasset.model.domain.other.auth.api.AuthAbstract;
import com.orientechnologies.orient.core.metadata.schema.OType;

/**
 * 
 *
 */
@I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH_ACCESS_LEVEL)
@DatabaseVertrex()
@ModelClassAnnotation(
		name = CLASS.AUTH_ACCESS_LEVEL,
		accessLevel=AuthAccessLevel.NONE,
		disableProperty = {
				AuthAbstract.CLASS_TYPE
				,AuthAbstract.CREATION_DATE 
				,AuthAbstract.ENABLE
				,AuthAbstract.DISABLE
				,AuthAbstract.ICON_TYPE
				,AuthAbstract.UPDATE_DATE
				,AuthAbstract.DESCRIPTION
		},
		canTrace = false,
		canGraph = false
		)
public class AuthAccessLevelClass extends AuthAbstract {

	private static Logger logger = LoggerFactory.getLogger(AuthAccessLevelClass.class);


	public AuthAccessLevelClass() {
		super();
	}
	
	public AuthAccessLevelClass(final BaseAbstract abase) {
		super(abase);
	}

	@ModelDefaultConstructorAnnotation()
	@ModelConstructorAnnotation()
	public AuthAccessLevelClass(
			final UserContextInterface context,
			final String name
			) {
		super(RootUserContext.ROOT_USER_CONTEXT, name);
		
	}

	public AuthAccessLevelClass(AuthAccessLevel authAccessLevel) {
		
		AuthAccessLevelClass authAccessLevelClass = getAuthAccessLevelClass(authAccessLevel);
		
		if(authAccessLevelClass == null) {
			authAccessLevelClass = new AuthAccessLevelClass(RootUserContext.ROOT_USER_CONTEXT, String.format("%s - %s", authAccessLevel.ordinal(), authAccessLevel.toString()));
		}
		
		this.setOrientVertex(authAccessLevelClass);
		this.setEnum(authAccessLevel.toString());
		this.setOrdinale(authAccessLevel.ordinal());
		this.commit();
		
	}

	
	// TODO fair un cache
	
	
	public static AuthAccessLevelClass getAuthAccessLevelClass(AuthAccessLevel authAccessLevel) {
		
		
		String cmdSql = "SELECT FROM " + AuthAccessLevelClass.class.getSimpleName() + " WHERE " + ENUM + " = ?";
		
		
		
		BaseQuery bq = new BaseQuery(RootUserContext.ROOT_USER_CONTEXT);

		@SuppressWarnings("unchecked")
		List<AuthAccessLevelClass> list = (List<AuthAccessLevelClass>) (List<?>) bq.commandBaseAbstract(cmdSql, authAccessLevel.toString());

		
		//List<AuthAccessLevelClass> list = (List<AuthAccessLevelClass> ) (List<?> ) BaseAbstract.commandBaseAbstract(RootUserContext.ROOT_USER_CONTEXT, cmdSql, authAccessLevel.toString());
		
		if(list.isEmpty()) 
		{
			if(logger.isTraceEnabled()) logger.trace("OrientVertex not found for 'id':" + authAccessLevel.toString());
			return null;
		}
		
		if(list.size() > 1)
		{
			throw new RuntimeException(new Exception("To many obejct for unique 'name':" +  authAccessLevel.toString()));
		}
		
		
		if(logger.isTraceEnabled()) logger.trace("OrientVertex found for 'id':" + authAccessLevel.toString()) ;
		return list.get(0);
	}
	
	/*
	 * 
	 */
	
	public static final String ENUM = "enum";
	
	@DatabaseProperty(name=ENUM, isNotNull = true)
	@ModelPropertyAnnotation(name = ENUM, type = TypeProperty.GET)
	public String getEnum()  {
		return this.getProperty(ENUM);
	}
	
	private void setEnum(String value) {
		this.setProperty(ENUM, value);
	}
	
	/*
	 * 
	 */
	
	public static final String ORDINAL = "ordinal";
	
	@DatabaseProperty(name=ORDINAL, isNotNull = true, type = OType.INTEGER)
	@ModelPropertyAnnotation(name = ORDINAL, type = TypeProperty.GET)
	public Integer getOrdinale()  {
		return this.getProperty(ORDINAL);
	}
	
	private void setOrdinale(Integer value) {
		this.setProperty(ORDINAL, value);
	}
	
	


	public AuthAccessLevel getAuthAccessLevel() {
		return AuthAccessLevel.valueOf(getEnum());
	}


	public static void init() {
		
		for(AuthAccessLevel authAccessLevel : AuthAccessLevel.values()) {
			new AuthAccessLevelClass(authAccessLevel);
		}
		
		
	}
	
}



