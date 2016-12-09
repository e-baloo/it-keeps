

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
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


	public static jGroup create(Rid requesteurRid, jGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminGroupCreate())
			throw ExceptionPermission.DENY;

		vGroup group = new vGroup(j);
		
		return fGroup.read(requesteurRid, group.getRid());
	}


	public static jGroup delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminGroupCreate())
			throw ExceptionPermission.DENY;


		vGroup group = vBaseAbstract.get(null, vGroup.class, rid, false);
		jGroup j = group.read();
		
		group.delete();
		
		return j;
	}

	public static jGroup read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if(!sAcl.isAdminGroupRead())
			throw ExceptionPermission.DENY;

		
		// TODO Security

		vGroup group = vBaseAbstract.get(null, vGroup.class, rid, false);
		
		return group.read();
	}
	
	public static jGroup update(Rid requesteurRid, jGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminGroupUpdate())
			throw ExceptionPermission.DENY;
		
		vGroup group = vBaseAbstract.get(null, vGroup.class, j.getRid(), false);

		group.checkVersion(j);
		group.update(j);
		
		return fGroup.read(requesteurRid, j.getRid());
	}


	public static List<jBaseLight> readAll(Rid requesterRid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminGroupRead())
			throw ExceptionPermission.DENY;

		return vBase.getAllBase(null, vGroup.class, false).stream().map(e -> e.read().getJBaseLight()).collect(Collectors.toList());
	}
}



