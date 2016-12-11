
package org.ebaloo.itkeeps.core.domain.vertex;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;

import org.ebaloo.itkeeps.api.model.jUser;

@DatabaseVertrex()
public final class vCredential extends vBase {

	private static final String HASH_PASSWORD = "hashPassword";

	protected vCredential() {
		super();
	}

	/*
	 * PARENT USER
	 */

	vCredential(final jCredential j, final jBaseLight jblUser) {
		super(j.getCred());

		try {
			if (StringUtils.isEmpty(j.getCred())) {
				throw new RuntimeException("TODO"); // TODO
			}

			if (jblUser != null) {
				vCredential.get(this.getGraph(), this.getClass(), j.getCred(), false);
				
				this.setUser(jblUser);
			}

			if (j.getAuthenticationType().equals(enAuthentication.BASIC)
					|| j.getAuthenticationType().equals(enAuthentication.TOKEN)) {
				this.setPassowrd64(j.getPassword64());
			}

			this.setAuthenticationType(j.getAuthenticationType());

            if ((jblUser == null) || jblUser.getId() == null) {

				jUser juser = new jUser();
				juser.setName(j.getUserName());

				vUser user = new vUser(juser, true);
				this.setUser(getJBaseLight(user));

			}

		} catch (Exception e) {
			e.printStackTrace();
			this.delete();
			throw e;
		}

	}

	boolean checkHashPassword(final String value) {

		if (StringUtils.isNoneEmpty(value))
			return false;

		String hash = this.getProperty(jCredential.PASSWORD64);

		if (StringUtils.isNoneEmpty(hash))
			return false;

		return SecurityFactory.checkHash(value, hash);
	}

	enAuthentication getAuthenticationType() {
		return enAuthentication.valueOf(this.getProperty(jCredential.AUTHENTICATION_TYPE));
	}

	/*
	 * PASSWORD
	 */

	private void setAuthenticationType(enAuthentication authType) {
		this.setProperty(jCredential.AUTHENTICATION_TYPE, authType.name());
	}

	vUser _getUser() {
		return this.getEdge(vUser.class, DirectionType.PARENT, false, eCredentialToUser.class);
	}

	public jBaseLight getUser() {
		return getJBaseLight(this._getUser());
	}

	private void setUser(jBaseLight user) {
		setEdges(this.getGraph(), vCredential.class, this, vUser.class, get(this.getGraph(), vUser.class, user, false), DirectionType.PARENT,
				eCredentialToUser.class, false);
	}

	private void setPassowrdHash(final String hash) {

		if (StringUtils.isEmpty(hash)) {
			this.setProperty(HASH_PASSWORD, StringUtils.EMPTY);
			return;
		}

		this.setProperty(HASH_PASSWORD, hash);
	}

	void setPassowrd64(final String base64) {

		if (StringUtils.isEmpty(base64)) {
			this.setProperty(HASH_PASSWORD, StringUtils.EMPTY);
			return;
		}

		this.setPassowrdHash(SecurityFactory.getHash(base64));
	}

	jCredential read() {
		jCredential j = new jCredential();
		
		this.readBase(j);

		j.setCred(this.getName());
		j.setName(this.getName());
		j.setUser(this.getUser());
		j.setAuthenticationType(this.getAuthenticationType());
		
		return j;
	}
	

	void update(jCredential j) {
		
		if(j.isPresentPassword64())
			this.setPassowrd64(j.getPassword64());
		
		
		if(j.isPresentUser() && (this.getUser() == null))
			this.setUser(j.getUser());
			
		
	}

}
