 
package org.ebaloo.itkeeps;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * 
 * 
 */
public final class Guid {
	
	//------------------------

	public static final String GUID = "guid";

	@JsonIgnore
	private UUID uuid = null;

	@JsonValue
	public final String getUuid() {
		return this.toString();
	}

	@JsonValue
	public final void setUuid(String uuid) {
		this.uuid = UUID.fromString(uuid);
	}


	public Guid() {
		this.uuid =  UUID.randomUUID();
	}

	
	public Guid(UUID uuid) {
		this.uuid =  uuid;
	}
	
	public Guid(String guid) {
		this.uuid = UUID.fromString(guid);
	}
	
	
	@JsonIgnore
	public String toString() {
		return this.uuid.toString();
	}
	
	@JsonIgnore
	public boolean equals(Object guid) {
		return guid instanceof Guid && this.uuid.equals(((Guid) guid).uuid);
	}

	@JsonIgnore
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
