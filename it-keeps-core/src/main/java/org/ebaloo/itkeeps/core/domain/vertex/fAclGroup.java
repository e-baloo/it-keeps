

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jAclGroup;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fAclGroup {



	public static jAclGroup create(Rid requesteurRid, jAclGroup j) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;
		vAclGroup aclGroup = new vAclGroup(j);
		return fAclGroup.read(requesteurRid, aclGroup.getRid());
	}


	public static jAclGroup delete(Rid requesteurRid, Rid rid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;
		
		vAclGroup aclGroup = vBaseAbstract.get(null, vAclGroup.class, rid, false);
		if(aclGroup != null) {
			jAclGroup j = aclGroup.read();
			aclGroup.delete();
			return j;
		}
		
		return null;
	}


	public static jAclGroup read(Rid requesteurRid, Rid rid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;
		vAclGroup aclGroup = vBaseAbstract.get(null, vAclGroup.class, rid, false);
		return aclGroup == null ? null : aclGroup.read();
	}

	
	public static jAclGroup update(Rid requesteurRid, jAclGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vAclGroup aclGroup = vBaseAbstract.get(null, vAclGroup.class, j.getRid(), false);

		aclGroup.checkVersion(j);
		aclGroup.update(j);
		
		return fAclGroup.read(requesteurRid, j.getRid());
	}
	
	


	public static List<jAclGroup> readAll(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		return vBase.getAllBase(null, vAclGroup.class, false).stream().map(vAclGroup::read).collect(Collectors.toList());
	}
}



