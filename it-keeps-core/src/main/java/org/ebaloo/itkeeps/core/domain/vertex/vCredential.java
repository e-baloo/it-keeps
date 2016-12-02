
package org.ebaloo.itkeeps.core.domain.vertex;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
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
		super(j.getId());

		try {
			if (StringUtils.isEmpty(j.getId())) {
				throw new RuntimeException("TODO"); // TODO
			}

			if (jblUser != null) {
				if (vCredential.get(this.getGraph(), this.getClass(), j.getId(), false) != null) {
					throw new RuntimeException("TODO"); // TODO
				}
			}

			if (j.getAuthenticationType().equals(enAuthentication.BASIC)
					|| j.getAuthenticationType().equals(enAuthentication.TOKEN)) {
				this.setPassowrd64(j.getPassword64());
			}

			this.setAuthenticationType(j.getAuthenticationType());

			if ((jblUser == null) || jblUser.getRid() == null) {

				jUser juser = new jUser();
				juser.setName(j.getUserName());

				vUser user = new vUser(juser, true);
				this.setUser(user);

			}

		} catch (Exception e) {
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
		return (enAuthentication) enAuthentication.valueOf(this.getProperty(jCredential.AUTHENTICATION_TYPE));
	}

	/*
	 * PASSWORD
	 */

	vUser _getUser() {
		return this.getEdge(vUser.class, DirectionType.PARENT, false, eCredentialToUser.class);
	}

	public jBaseLight getUser() {
		return getJBaseLight(this._getUser());
	}

	
	private void setAuthenticationType(enAuthentication authType) {
		this.setProperty(jCredential.AUTHENTICATION_TYPE, authType.name());
	}

	private void setPassowrdHash(final String hash) {

		if (StringUtils.isNoneEmpty(hash)) {
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

	/*
	 * AUTHENTICATION_TYPE
	 */

	private void setUser(jBaseLight user) {
		this.setUser(get(this.getGraph(), vUser.class, user, false));
	}

	private void setUser(final vUser user) {
		setEdges(this.getGraph(), vCredential.class, this, vUser.class, user, DirectionType.PARENT,
				eCredentialToUser.class, false);
	}

	// API

	/*
	 * public Credential(final JUser j) { this(j, true); }
	 * 
	 * protected Credential(final JUser j, final boolean f) { super(j, false);
	 * 
	 * this.commit(); this.reload();
	 * 
	 * if(Credential.getById(this.getGraph(), j.getUserId()) != null) throw new
	 * RuntimeException("TODO"); //TODO
	 * 
	 * if(StringUtils.isEmpty(j.getRole()))
	 * j.setRole(SecurityRole.GUEST.toString());
	 * 
	 * this.setUserId(j.getUserId()); this.setRole(j.getRole());
	 * 
	 * if(f) this.setEnable(Boolean.TRUE); }
	 * 
	 * 
	 * @Override public <T extends JBase> T apiFill(T j, Guid requesteurGuid) {
	 * 
	 * if(!(j instanceof JUser)) throw new RuntimeException("TODO"); //TODO
	 * 
	 * super.apiFill(j, requesteurGuid);
	 * 
	 * JUser juser = (JUser) j;
	 * 
	 * juser.setUserId(this.getUserId());
	 * juser.setRole(this.getRole().toString());
	 * juser.setInGroups(this.getInGroup().stream().map(e ->
	 * Base.getJBaseLight(e)).collect(Collectors.toList()));
	 * 
	 * return j; }
	 * 
	 * @Override public <T extends JBase> void apiUpdate(T obj, Guid
	 * requesteurGuid) {
	 * 
	 * if(!(obj instanceof JUser)) throw new RuntimeException("TODO"); //TODO
	 * 
	 * super.apiUpdate(obj, requesteurGuid);
	 * 
	 * 
	 * Credential requesterUser = Credential.get(this.getGraph(),
	 * ModelFactory.get(Credential.class), requesteurGuid, false);
	 * 
	 * switch(requesterUser.getRole()) {
	 * 
	 * case ROOT: // -> Ok is root break;
	 * 
	 * case ADMIN: //TODO Check it requesterUser have the right to update this
	 * break;
	 * 
	 * case USER: if(!this.getGuid().equals(requesterUser.getGuid())) { throw
	 * new RuntimeException("TODO"); //TODO } break;
	 * 
	 * case GUEST: default: throw new RuntimeException("TODO"); //TODO }
	 * 
	 * 
	 * JUser juser = (JUser) obj;
	 * 
	 * if(juser.isPresentRole()) {
	 * 
	 * SecurityRole requesterRole = requesterUser.getRole(); SecurityRole jRole
	 * = SecurityRole.valueOf(juser.getRole());
	 * 
	 * if(requesterRole.isRoot()) { this.setRole(jRole); } else
	 * if(requesterRole.isAdmin() && !jRole.isRoot()) { this.setRole(jRole); }
	 * else { throw new RuntimeException(""); //TODO } }
	 * 
	 * if(juser.isPresentUserId()) this.setIcon(juser.getIcon());
	 * 
	 * 
	 * // ADMIN & ROOT
	 * 
	 * if(requesterUser.getRole().isAdmin()) {
	 * 
	 * 
	 * if(juser.isChildGroups()) { this.setInGroupJBL(juser.getInGroups()); }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

}
