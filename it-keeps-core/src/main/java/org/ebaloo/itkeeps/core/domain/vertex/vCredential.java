
package org.ebaloo.itkeeps.core.domain.vertex;



import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.jUser;



@DatabaseVertrex()
public final class vCredential extends vBase {

	private static Logger logger = LoggerFactory.getLogger(vCredential.class);

	
	protected vCredential() {
		super();
	}
	
	
	public vCredential(final jCredential j, final jBaseLight jblUser) {
		super(j.getId());
		
		try {
		if(StringUtils.isEmpty(j.getId())) {
			throw new RuntimeException("TODO"); //TODO
		}
		
		if(jblUser != null) {
			if(vCredential.get(this.getGraph(), this.getClass(), j.getId(), false) != null) {
				throw new RuntimeException("TODO"); //TODO
			}
		}
		
		
		if(j.getAuthenticationType().equals(enAuthentication.BASIC) || j.getAuthenticationType().equals(enAuthentication.TOKEN) ) {
			this.setPassowrd(j.getPassword());
		}
	
		this.setAuthenticationType(j.getAuthenticationType());
		
		if((jblUser == null) || jblUser.getGuid() == null) {
			
			jUser juser = new jUser();
			juser.setName(j.getUserName());

			vUser user = new vUser(juser);
			this.setUser(user);
			
		}

	} catch (Exception e) {
		this.delete();
		throw e;
	}

		
	}
	
	
	/*
	 * PARENT USER
	 */
	
	public vUser getUser() {
		return this.getEdge(vUser.class, DirectionType.PARENT, false, eCredentialToUser.class);
	}
	
	private void setUser(final vUser user) {
		setEdges(this.getGraph(), vCredential.class, this, vUser.class, user, DirectionType.PARENT, eCredentialToUser.class, false);
	}

	public void setUser(jBaseLight user) {
		this.setUser(get(this.getGraph(), vUser.class, user, false));
	}
	

	/*
	 *   PASSWORD
	 */
	

	@DatabaseProperty(name = jCredential.PASSWORD)
	private String getPassword()  {
		return this.getProperty(jCredential.PASSWORD);
	}

	public void setPassowrd(final String value) {
		
		this.setProperty(jCredential.PASSWORD, value == null ? "" : value);
	}
	

	/*
	 * AUTHENTICATION_TYPE
	 */
	
	@DatabaseProperty(name = jCredential.AUTHENTICATION_TYPE)
	public enAuthentication getAuthenticationType() {
		return (enAuthentication) enAuthentication.valueOf(this.getProperty(jCredential.AUTHENTICATION_TYPE));
	}
	
	private void setAuthenticationType(enAuthentication authType) {
		this.setProperty(jCredential.AUTHENTICATION_TYPE, authType.name());
	}

	// API
	
	/*
	public Credential(final JUser j) {
		this(j, true);
	}
	
	protected Credential(final JUser j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();

		if(Credential.getById(this.getGraph(), j.getUserId()) != null)
			throw new RuntimeException("TODO"); //TODO

		if(StringUtils.isEmpty(j.getRole()))
				j.setRole(SecurityRole.GUEST.toString());
		
		this.setUserId(j.getUserId());
		this.setRole(j.getRole());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	
	@Override
	public <T extends JBase> T apiFill(T j, Guid requesteurGuid) {
		
		if(!(j instanceof JUser))
			throw new RuntimeException("TODO"); //TODO
		
		super.apiFill(j, requesteurGuid);

		JUser juser = (JUser) j;
		
		juser.setUserId(this.getUserId());
		juser.setRole(this.getRole().toString());
		juser.setInGroups(this.getInGroup().stream().map(e -> Base.getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	@Override
	public <T extends JBase> void apiUpdate(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof JUser))
			throw new RuntimeException("TODO"); //TODO

		super.apiUpdate(obj, requesteurGuid);
		
		
		Credential requesterUser = Credential.get(this.getGraph(), ModelFactory.get(Credential.class), requesteurGuid, false);

		switch(requesterUser.getRole()) {

		case ROOT:
			// -> Ok is root
			break;

		case ADMIN:
			//TODO Check it requesterUser have the right to update this
			break;
			
		case USER:
			if(!this.getGuid().equals(requesterUser.getGuid())) {
				throw new RuntimeException("TODO"); //TODO
			}
			break;

		case GUEST:
		default:
			throw new RuntimeException("TODO"); //TODO
		}
		

		JUser juser = (JUser) obj;
		
		if(juser.isPresentRole()) {
			
			SecurityRole requesterRole = requesterUser.getRole();
			SecurityRole jRole = SecurityRole.valueOf(juser.getRole());
			
			if(requesterRole.isRoot()) {
				this.setRole(jRole);
			} else if(requesterRole.isAdmin() && !jRole.isRoot()) {
				this.setRole(jRole);
			} else {
				throw new RuntimeException(""); //TODO
			}
		}

		if(juser.isPresentUserId())
			this.setIcon(juser.getIcon());

		
		// ADMIN & ROOT
		
		if(requesterUser.getRole().isAdmin()) {
			
			
			if(juser.isChildGroups()) {
				this.setInGroupJBL(juser.getInGroups());
			}

			
		}
		
		
	}
*/

	
}



