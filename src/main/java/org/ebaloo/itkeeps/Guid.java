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

package org.ebaloo.itkeeps;

import java.util.UUID;

import com.jcdecaux.itasset.common.tools.StringUtils;


/**
 * 
 * 
 */
public final class Guid {
	
	//------------------------
	
	private UUID uuid = null;
	
	
	public Guid(UUID uuid) {
		this.uuid =  uuid;
	}
	
	public Guid(String guid) {
		this.uuid = UUID.fromString(guid);
	}
	
	public String toString() {
		return this.uuid.toString();
	}
	
	public boolean equals(Object guid) {
		return guid instanceof Guid && this.uuid.equals(((Guid) guid).uuid);
	}

	public static boolean isGuid(String guid) {
		try{
			
			if(StringUtils.isBlank(guid)) {
				return false;
			}
			
		    UUID.fromString(guid);
		} catch (Throwable exception){
		    return false; 
		}
		
		return true;
	}
	
}
