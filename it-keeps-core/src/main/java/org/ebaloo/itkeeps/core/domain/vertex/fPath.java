

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fPath {


	public static final jPath create(Rid requesteurRid, jPath j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vPath path = new vPath(j);
		
		return fPath.read(requesteurRid, path.getRid());
	}


	public static final jPath delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		vPath path = vBaseAbstract.get(null, vPath.class, rid, false);
		jPath j = path.read();
		
		path.delete();
		
		return j;
	}

	public static final jPath read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vPath path = vBaseAbstract.get(null, vPath.class, rid, false);
		
		return path.read();
	}

	public static final jPath update(Rid requesteurRid,  jPath j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vPath path = vBaseAbstract.get(null, vPath.class, j.getRid(), false);

		path.checkVersion(j);
		path.update(j);
		
		return fPath.read(requesteurRid, j.getRid());
	}
	
	


	public static final List<jPath> readAll(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		List<jPath> list = vBase.getAllBase(null, vPath.class, false).stream().map(e -> e.read()).collect(Collectors.toList());
		
		return list;
	}
}



