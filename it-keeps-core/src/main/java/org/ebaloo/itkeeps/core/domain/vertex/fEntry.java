

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fEntry {


	public static final jEntry create(Rid requesteurRid, jEntry j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vEntry entry = new vEntry(j);
		
		return fEntry.read(requesteurRid, entry.getRid());
	}


	public static final jEntry delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		jEntry j = entry.read();
		
		entry.delete();
		
		return j;
	}

	public static final jEntry read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		
		return entry.read();
	}

	public static final jEntry update(Rid requesteurRid,  jEntry j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, j.getRid(), false);

		entry.checkVersion(j);
		entry.update(j);
		
		return fEntry.read(requesteurRid, j.getRid());
	}
	
	


	public static final List<jEntry> readAll(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		// TODO Security
		
		List<jEntry> list = vBase.getAllBase(null, vEntry.class, false).stream().map(e -> e.read()).collect(Collectors.toList());
		
		return list;
	}
}



