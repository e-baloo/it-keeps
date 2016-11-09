
package org.ebaloo.itkeeps.domain;

import java.util.UUID;

import org.ebaloo.itkeeps.domain.vertex.Base;
import org.ebaloo.itkeeps.domain.vertex.CommonOrientVertex;


public class GuidFactory {
	
	
	public static boolean isExist(final Guid guid)
	{
		String cmdSql = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + Base.GUID + " = ?";
		
		return CommonOrientVertex.command(cmdSql, guid.toString()).size() > 0;
	}

	
	public static Guid getGuid() {
		
		Guid guid = new Guid(UUID.randomUUID());
		
        while(isExist(guid)) {
        	guid = new Guid(UUID.randomUUID());
        }
        	
		return guid;
	}
	
}
