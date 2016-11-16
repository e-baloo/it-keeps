
package org.ebaloo.itkeeps.core.domain;

import java.util.UUID;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.vertex.Base;


public class GuidFactory {
	
	
	public static boolean isExist(final Guid guid)
	{
		String cmdSql = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + JBase.GUID + " = ?";
		
		return GraphFactory.command(null, cmdSql, guid.toString()).size() > 0;
	}

	
	public static Guid getGuid() {
		
		Guid guid = new Guid(UUID.randomUUID());
		
        while(isExist(guid)) {
        	guid = new Guid(UUID.randomUUID());
        }
        	
		return guid;
	}
	
}
