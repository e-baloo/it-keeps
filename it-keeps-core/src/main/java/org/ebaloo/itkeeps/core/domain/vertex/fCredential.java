
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jUser;

@DatabaseVertrex()
public final class fCredential {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(fCredential.class);

	

	
	
	
	// API

	
	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		/*
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleAdmin())
			throw ExceptionPermission.NOT_ADMIN;
		if(!sAcl.isAdminUserRead())
			throw ExceptionPermission.DENY;
		*/
		
		jUser j = fUser.read(requesteurRid, requesteurRid);
		
		return j.getCredentials();
	}

	
	
	
	public static final jCredential read(Rid requesteurRid, Rid credRid) {

		/*
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, credRid);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		if ((!sAcl.isRoleRoot() || !sAcl.isRoleAdmin()) && !credRid.equals(requesteurRid))
			throw ExceptionPermission.IS_USER;
		if (!credRid.equals(requesteurRid) && !sAcl.isAdminUserRead())
			throw ExceptionPermission.NOT_USER_READ;
		*/

		jUser j = fUser.read(requesteurRid, requesteurRid);

		jCredential jc = null;
		
		if(j.getCredentials().contains(credRid)) {
			jc = vCredential.get(null, vCredential.class, credRid, false).read();
		}
		
		return jc;
	}

	public static final jCredential delete(Rid requesteurRid, Rid credRid) {

		jUser j = fUser.read(requesteurRid, requesteurRid);

		jCredential jc = null;
		
		if(j.getCredentials().contains(credRid)) {
			
			vCredential vc = vCredential.get(null, vCredential.class, credRid, false);
			
			if(vc != null) {
				jc = vc.read();
				vc.delete();
			}
		}


		return jc;
	}

	public static final jCredential create(Rid requesteurRid, jCredential j) {

		/*
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);

		if (!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if (!sAcl.isAdminUserCreate())
			throw ExceptionPermission.NOT_USER_CREATE;
			*/

		jUser ju = fUser.read(requesteurRid, requesteurRid);
		
		vCredential vc = new vCredential(j, ju.getJBaseLight());
		
		return fCredential.read(requesteurRid, vc.getRid());
	}
	
	
	

	public static final void update(Rid requesteurRid, jCredential j) {
		fUser.read(requesteurRid, requesteurRid);
		vCredential cred = vCredential.get(null, vCredential.class, j.getRid(), false);
		cred.update(j);
	}


}
