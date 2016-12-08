
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;

@DatabaseVertrex()
public final class fCredential {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(fCredential.class);

	

	
	
	
	// API

	
	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		jUser j = fUser.read(requesteurRid, requesteurRid);
		return j.getCredentials();
	}

	
	public static final jCredential read(Rid requesteurRid, Rid credRid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);
		return isUserCerd(requesteurRid, credRid) || sAcl.isRoleRoot() ? vCredential.get(null, vCredential.class, credRid, false).read() : null;
	}

	
	private static boolean isUserCerd(Rid requesteurRid, Rid credRid) {
		jUser j = fUser.read(requesteurRid, requesteurRid);
		return j.getCredentials().contains(credRid);
	}
	
	
	public static final jCredential delete(Rid requesteurRid, Rid credRid) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;

		jCredential jc = null;
		
		if(isUserCerd(requesteurRid, credRid) || sAcl.isRoleRoot()) {
			
			vCredential vc = vCredential.get(null, vCredential.class, credRid, false);
			
			if(vc != null) {
				jc = vc.read();
				vc.delete();
			}
		}

		return jc;
	}

	public static final jCredential create(Rid requesteurRid, jCredential j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;

		jUser ju = fUser.read(requesteurRid, requesteurRid);
		
		vCredential vc = new vCredential(j, ju.getJBaseLight());
		
		return fCredential.read(requesteurRid, vc.getRid());
	}
	
	
	public static final jCredential create(Rid requesteurRid, Rid userRid , jCredential j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);

		if (!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		jUser ju = fUser.read(requesteurRid, userRid);
		
		vCredential vc = new vCredential(j, ju.getJBaseLight());

		return fCredential.read(requesteurRid, vc.getRid());
	}
	
	

	public static final void update(Rid requesteurRid, jCredential j) {
		fUser.read(requesteurRid, requesteurRid);
		vCredential cred = vCredential.get(null, vCredential.class, j.getRid(), false);
		cred.update(j);
	}


}
