

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


    public static jAcl create(Rid requesterRid, jAcl j) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);

        if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclCreate())
			throw ExceptionPermission.DENY;

		vAcl acl = new vAcl(j);

        return fAcl.read(requesterRid, acl.getRid());
    }


    public static jAcl delete(Rid requesterRid, Rid rid) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);
        if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclCreate())
			throw ExceptionPermission.DENY;


		vAcl acl = vBaseAbstract.get(null, vAcl.class, rid, false);
		jAcl j = acl.read();
		
		acl.delete();
		
		return j;
	}

    public static jAcl read(Rid requesterRid, Rid rid) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);
        if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if(!sAcl.isAdminAclRead())
			throw ExceptionPermission.DENY;

		
		// TODO Security

		vAcl acl = vBaseAbstract.get(null, vAcl.class, rid, false);
		
		return acl.read();
	}

    public static jAcl update(Rid requesterRid, jAcl j) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);
        if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclUpdate())
			throw ExceptionPermission.DENY;
		
		vAcl acl = vBaseAbstract.get(null, vAcl.class, j.getRid(), false);

		acl.checkVersion(j);
		acl.update(j);

        return fAcl.read(requesterRid, j.getRid());
    }


    public static List<jBaseLight> readAll(Rid requesterRid) {
        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);
        if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminAclRead())
			throw ExceptionPermission.DENY;

        return vBase.getAllBase(null, vAcl.class, false).stream().map(e -> e.read().getLight()).collect(Collectors.toList());
    }
}



