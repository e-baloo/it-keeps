
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.List;
import java.util.stream.Collectors;

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


	public static List<jBaseLight> readAll(Rid requesterRid) {
		jUser j = fUser.read(requesterRid, requesterRid);
		return j.getCredentials();
	}


	public static jCredential read(Rid requesterRid, Rid credRid) {
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);
		return isUserCerd(requesterRid, credRid) || sAcl.isRoleRoot() ? vCredential.get(null, vCredential.class, credRid, false).read() : null;
	}


	private static boolean isUserCerd(Rid requesterRid, Rid credRid) {
		jUser j = fUser.read(requesterRid, requesterRid);

		if(j.getCredentials() == null)
			return false;

        return j.getCredentials().stream().map(jBaseLight::getId).collect(Collectors.toList()).contains(credRid);
    }


	public static jCredential delete(Rid requesterRid, Rid credRid) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;

		jCredential jc = null;

		if (isUserCerd(requesterRid, credRid) || sAcl.isRoleRoot()) {
			
			vCredential vc = vCredential.get(null, vCredential.class, credRid, false);
			
			if(vc != null) {
				jc = vc.read();
				vc.delete();
			}
		}

		return jc;
	}

	public static jCredential create(Rid requesterRid, jCredential j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

		if (sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;

		jUser ju = fUser.read(requesterRid, requesterRid);

        vCredential vc = new vCredential(j, ju.getLight());

		return fCredential.read(requesterRid, vc.getRid());
	}


	public static jCredential create(Rid requesterRid, Rid userRid, jCredential j) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

		if (!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		jUser ju = fUser.read(requesterRid, userRid);

        vCredential vc = new vCredential(j, ju.getLight());

		return fCredential.read(requesterRid, vc.getRid());
	}


	public static void update(Rid requesterRid, jCredential j) {
		fUser.read(requesterRid, requesterRid);
		vCredential cred = vCredential.get(null, vCredential.class, j.getId(), false);
		cred.update(j);
	}


}
