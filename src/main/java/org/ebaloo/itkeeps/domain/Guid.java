 
package org.ebaloo.itkeeps.domain;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;


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
