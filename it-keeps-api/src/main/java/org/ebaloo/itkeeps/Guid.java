 
package org.ebaloo.itkeeps;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 
 * 
 */
public final class Guid {
	
	//------------------------

	public static final String GUID = "guid";

	private UUID uuid = null;

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
