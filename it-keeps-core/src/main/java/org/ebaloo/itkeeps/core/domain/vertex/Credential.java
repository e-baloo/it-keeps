
package org.ebaloo.itkeeps.core.domain.vertex;



import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.annotation.ApplicationRolesAllowed.SecurityRole;
import org.ebaloo.itkeeps.api.enumeration.AuthenticationType;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.CredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.InGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.JUser;



@ModelClassAnnotation()
@DatabaseVertrex()
public final class Credential extends Base {

	private static Logger logger = LoggerFactory.getLogger(Credential.class);

	
	protected Credential() {
		super();
	}
	
	protected Credential(final BaseAbstract abase) {
		super(abase);
	}

	
	public Credential(final JCredential j, final JBaseLight jblUser) {
		super(j.getId());
		
		if(StringUtils.isEmpty(j.getId())) {
			this.disable();
			throw new RuntimeException("TODO"); //TODO
		}
		
		if(jblUser != null) {
			if(Credential.get(this.getGraph(), this.getModelClass(), j.getId(), false) != null) {
				this.disable();
				throw new RuntimeException("TODO"); //TODO
			}
		}
		
		
		if(j.getAuthenticationType().equals(AuthenticationType.BASIC) || j.getAuthenticationType().equals(AuthenticationType.TOKEN) ) {
			this.setPassowrd(j.getPassword());
		}
	
		this.setAuthenticationType(j.getAuthenticationType());
		
		if((jblUser == null) || jblUser.getGuid() == null) {
			
			logger.trace("create user");
			
			JUser juser = new JUser();
			juser.setName(j.getUserName());
			juser.setRole(SecurityRole.GUEST);
			
			User user = new User(juser);
			this.setUser(user);
			
			logger.trace("Cred : " + this.toString());
			logger.trace("User : " + user.toString());
			
		}
		
		this.setEnable(Boolean.TRUE);
		
	}
	
	/*
	 * PARENT USER
	 */
	
	public User getUser() {
		return this.getEdgeByClassesNames(ModelFactory.get(User.class), DirectionType.PARENT, false, CredentialToUser.class);
	}
	
	private void setUser(final User user) {
		setEdges(this.getGraph(), ModelFactory.get(Group.class), this, user, DirectionType.PARENT, CredentialToUser.class, false);
	}

	public void setUser(JBaseLight user) {
		this.setUser(get(this.getGraph(), ModelFactory.get(User.class), user, false));
	}
	

	/*
	 *   PASSWORD
	 */
	

	@DatabaseProperty(name = JCredential.PASSWORD)
	private String getPassword()  {
		return this.getProperty(JCredential.PASSWORD);
	}

	public void setPassowrd(final String value) {
		
		this.setProperty(JCredential.PASSWORD, value == null ? "" : value);
	}
	
	
	
	
	/*
	 * ROLE
	 */
	
	@DatabaseProperty(name = JCredential.AUTHENTICATION_TYPE)
	public AuthenticationType getAuthenticationType() {
		return (AuthenticationType) AuthenticationType.valueOf(this.getProperty(JCredential.AUTHENTICATION_TYPE));
	}
	
	private void setAuthenticationType(AuthenticationType authType) {
		this.setProperty(JCredential.AUTHENTICATION_TYPE, authType.toString());
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



