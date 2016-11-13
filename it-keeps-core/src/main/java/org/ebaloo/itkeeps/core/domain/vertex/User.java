
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.List;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.restapp.authentication.ApplicationRolesAllowed.SecurityRole;
import org.ebaloo.itkeeps.core.tools.SecurityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.JUser;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;


@ModelClassAnnotation()
@DatabaseVertrex()
public class User extends BaseStandard {

	private static Logger logger = LoggerFactory.getLogger(User.class);

	
	public User() {
		super();
	}
	
	public User(final BaseAbstract abase) {
		super(abase);
	}


	
	/*
	 * 
	 * 
	 */
	
	
	
	
	/*
	 *   ID
	 */
	

	@DatabaseProperty(name = JUser.ID, isNotNull = true)
	public String getId()  {
		return this.getProperty(JUser.ID);
	}

	public void setId(final String id) {
		this.setProperty(JUser.ID, id);
	}

	
	
	public static User getByCredentials(final JCredential credentials) {

		if(credentials == null) 
			throw new RuntimeException("TODO"); //TODO
		
		User user = getById(credentials.getId());
		
		SecurityFactory.validateCredential(user, credentials);

		return user;
	}

	
	public static User getById(final String id) {

		if(StringUtils.isEmpty(id))
			throw new RuntimeException("TODO"); //TODO
		
		
		String cmdSql = "SELECT FROM " + User.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable() + " AND " + JUser.ID
				+ ".toLowerCase() = ?";

		List<OrientVertex> list = CommonOrientVertex.command(cmdSql, id.toLowerCase());

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for 'id':" + id);
			return null;
		}

		if (list.size() > 1) {
			throw new RuntimeException("To many obejct for 'id': " + id);
		}

		OrientVertex ov = list.get(0);

		User user = new User();
		user.setOrientVertex(ov);
		
		if (logger.isTraceEnabled())
			logger.trace("User found for 'id':" +  id + " @" + ov.getType().getName() + " "
					+ ov.getIdentity().toString());
		
		return user;
	}
	
	/*
	 * ROLES
	 */
	
	/*
	
	@DatabaseProperty(name = JUser.ROLES, type = OType.EMBEDDEDLIST)
	public List<String> getRoles() {
		return this.getEmbeddedListString(JUser.ROLES);
	}
	
	
	public void setRoles(List<String> roles) {
		
		if(roles == null)
			roles = new ArrayList<>();
		
		this.setProperty(JUser.ROLES, roles);
	}
	*/


	/*
	 * ROLE
	 */
	
	@DatabaseProperty(name = JUser.ROLE)
	public SecurityRole getRole() {
		return SecurityRole.valueOf(this.getProperty(JUser.ROLE));
	}
	
	public void setRole(String role) {
		this.setRole(SecurityRole.valueOf(role));
	}
	
	public void setRole(SecurityRole role) {
		this.setProperty(JUser.ROLE, role.toString());
	}

	// API
	
	@Override
	public <T extends JBase> void apiFill(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof JUser))
			throw new RuntimeException("TODO"); //TODO
		
		super.apiFill(obj, requesteurGuid);

		JUser juser = (JUser) obj;
		
		juser.setId(this.getId());
		juser.setRole(this.getRole().toString());
		
	}
	
	@Override
	public <T extends JBase> void apiUpdate(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof JUser))
			throw new RuntimeException("TODO"); //TODO

		super.apiUpdate(obj, requesteurGuid);
		
		
		User requesterUser = User.getByGuid(ModelFactory.get(User.class), requesteurGuid);

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
		
		if(juser.isPresentId())
			this.setIcon(juser.getIcon());

		if(juser.isPresentRole())
			this.setRole(juser.getRole());

	}


	
}



