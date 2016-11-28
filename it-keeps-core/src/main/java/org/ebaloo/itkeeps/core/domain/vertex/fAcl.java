

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fAcl {


	public static final jAcl create(Rid requesteurRid, jAcl j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		
		System.out.println(sAcl);
		
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclCreate())
			throw ExceptionPermission.DENY;

		vAcl acl = new vAcl(j);
		
		return fAcl.read(requesteurRid, acl.getRid());
	}


	public static final jAcl delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclCreate())
			throw ExceptionPermission.DENY;


		vAcl acl = vBaseAbstract.get(null, vAcl.class, rid, false);
		jAcl j = acl.read();
		
		acl.delete();
		
		return j;
	}

	public static final jAcl read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if(!sAcl.isAdminAclRead())
			throw ExceptionPermission.DENY;

		
		// TODO Security

		vAcl acl = vBaseAbstract.get(null, vAcl.class, rid, false);
		
		return acl.read();
	}
	
	public static final jAcl update(Rid requesteurRid,  jAcl j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclUpdate())
			throw ExceptionPermission.DENY;
		
		vAcl acl = vBaseAbstract.get(null, vAcl.class, j.getRid(), false);

		acl.checkVersion(j);
		acl.update(j);
		
		return fAcl.read(requesteurRid, j.getRid());
	}
	
	


	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclRead())
			throw ExceptionPermission.DENY;

		List<jBaseLight> list = vBase.getAllBase(null, vAcl.class, false).stream().map(e -> e.read().getJBaseLight()).collect(Collectors.toList());
		return list;
	}
}



