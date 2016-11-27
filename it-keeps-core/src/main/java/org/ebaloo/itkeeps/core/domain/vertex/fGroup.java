

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fGroup {


	public static final jGroup create(Rid requesteurRid, jGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;

		// TODO Security
		
		vGroup group = new vGroup(j);
		
		return fGroup.read(requesteurRid, group.getRid());
	}


	public static final jGroup delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vGroup group = vBaseAbstract.get(null, vGroup.class, rid, false);
		jGroup j = group.read();
		
		group.delete();
		
		return j;
	}

	public static final jGroup read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		
		// TODO Security

		vGroup group = vBaseAbstract.get(null, vGroup.class, rid, false);
		
		return group.read();
	}

	public static final jGroup update(Rid requesteurRid,  jGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		
		// TODO Security

		vGroup group = vBaseAbstract.get(null, vGroup.class, j.getRid(), false);

		group.checkVersion(j);
		group.update(j);
		
		return fGroup.read(requesteurRid, j.getRid());
	}
	
	


	public static final List<jGroup> readAll(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		List<jGroup> list = vBase.getAllBase(null, vGroup.class, false).stream().map(e -> e.read()).collect(Collectors.toList());
		
		return list;
	}
}



