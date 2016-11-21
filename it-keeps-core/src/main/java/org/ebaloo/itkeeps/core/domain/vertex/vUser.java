
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.jUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


@DatabaseVertrex()
public class vUser extends vBaseStandard {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vUser.class);

	
	protected vUser() {
		super();
	}
	
	/*
	public User(final BaseAbstract abase) {
		super(abase);
	}
	*/


	/*
	public User(final JCredential j) {
		super(j.getUserName());
		
		if(User.getById(this.getGraph(), j.getId()) != null) {
			this.disable();
			throw new RuntimeException("TODO"); //TODO
		}
		
		if(StringUtils.isEmpty(j.getId())) {
			this.disable();
			throw new RuntimeException("TODO"); //TODO
		}

		this.setUserId(j.getId());
		this.setRole(SecurityRole.GUEST);
		
		this.setEnable(Boolean.TRUE);
		
	}
	*/
	

	/*
	 * IN_GROUP
	 */
	
	public List<vGroup> getGroups() {
		return this.getEdgesByClassesNames(vGroup.class, DirectionType.PARENT, false, eInGroup.class);
	}
	
	public void setGroups(List<vGroup> list) {
		setEdges(this.getGraph(), vUser.class, this, list, DirectionType.PARENT, eInGroup.class, false);
	}

	private void setGroupsJBL(List<jBaseLight> list) {
		
		if(list == null) {
			list = new ArrayList<jBaseLight>();
		}
		
		// Optimization
		OrientBaseGraph graph = this.getGraph();
		setGroups(list.stream().map(e -> get(graph, vGroup.class, e, false)).collect(Collectors.toList())); 
	}

	/*
	 * CREDENTIALS
	 */
	
	public List<vCredential> getCredentials() {
		return this.getEdgesByClassesNames(vCredential.class, DirectionType.CHILD, false, eCredentialToUser.class);
	}
	
	private void _setCredentials(List<vCredential> list) {
		setEdges(this.getGraph(), vUser.class, this, list, DirectionType.CHILD, eCredentialToUser.class, false);
	}

	public void setCredentials(List<jBaseLight> list) {
		
		if(list == null) {
			list = new ArrayList<jBaseLight>();
		}
		
		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		this._setCredentials(list.stream().map(e -> get(graph, vCredential.class, e, false)).collect(Collectors.toList())); 
	}
	
	
	/*
	 *  ID
	 */
	/*

	@DatabaseProperty(name = JUser.USER_ID, isNotNull = true)
	public String getUserId()  {
		return this.getProperty(JUser.USER_ID);
	}

	public void setUserId(final String id) {
		
		if(StringUtils.isEmpty(id))
			throw new RuntimeException("TODO"); //TODO
		
		this.setProperty(JUser.USER_ID, id);
	}

	
	
	public static User getByCredentials(OrientBaseGraph graph, final JCredential credentials) {

		if(credentials == null) 
			throw new RuntimeException("TODO"); //TODO
		
		User user = getById(graph, credentials.getId());
		
		SecurityFactory.validateCredential(user, credentials);

		return user;
	}
	

	
	public static User getById(OrientBaseGraph graph, final String id) {

		if(StringUtils.isEmpty(id))
			throw new RuntimeException("TODO"); //TODO
		
		
		String cmdSql = "SELECT FROM " + User.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND " + JUser.USER_ID
				+ ".toLowerCase() = ?";

		List<OrientVertex> list = CommonOrientVertex.command(graph, cmdSql, id.toLowerCase());

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
	*/
	
	/*
	 *   PASSWORD
	 */
	
/*
	@DatabaseProperty(name = JUser.PASSWORD)
	public String getPassword()  {
		return this.getProperty(JUser.PASSWORD);
	}

	public void setPassowrd(final String value) {
		
		this.setProperty(JUser.PASSWORD, value);
	}
	
	*/
	

	/*
	 * ROLE
	 */
	
	@DatabaseProperty(name = jUser.ROLE)
	public enSecurityRole getRole() {
		return enSecurityRole.valueOf(this.getProperty(jUser.ROLE));
	}
	
	public void setRole(String role) {
		this.setRole(enSecurityRole.valueOf(role));
	}
	
	public void setRole(enSecurityRole role) {
		this.setProperty(jUser.ROLE, role.toString());
	}

	// API
	
	public vUser(final jUser j) {
		this(j, true);
	}
	
	protected vUser(final jUser j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();

		/*
		if(User.getById(this.getGraph(), j.getUserId()) != null)
			throw new RuntimeException("TODO"); //TODO
			

		if(StringUtils.isEmpty(j.getRole()))
				j.setRole(SecurityRole.GUEST.toString());
		*/
		
		//this.setUserId(j.getUserId());
		if(j.isPresentRole())
			this.setRole(j.getRole());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends jBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new jUser();
		
		if(!(j instanceof jUser))
			throw new RuntimeException("TODO"); //TODO
		
		super.read(j, requesteurGuid);

		jUser juser = (jUser) j;
		
		//juser.setUserId(this.getUserId());
		if(juser.isPresentRole())
			juser.setRole(this.getRole());
		
		if(juser.isPresentInGroup())
			juser.setInGroups(this.getGroups().stream().map(e -> vBase.getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	@Override
	public <T extends jBase> T update(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof jUser))
			throw new RuntimeException("TODO"); //TODO

		super.update(obj, requesteurGuid);
		
		
		vUser requesterUser = vUser.get(this.getGraph(), vUser.class, requesteurGuid, false);

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
		

		jUser juser = (jUser) obj;
		
		if(juser.isPresentRole()) {
			
			enSecurityRole requesterRole = requesterUser.getRole();
			enSecurityRole jRole = juser.getRole();
			
			if(requesterRole.isRoot()) {
				this.setRole(jRole);
			} else if(requesterRole.isAdmin() && !jRole.isRoot()) {
				this.setRole(jRole);
			} else {
				throw new RuntimeException(""); //TODO
			}
		}

		/*
		if(juser.isPresentUserId())
			this.setIcon(juser.getIcon());
		 */
		
		// ADMIN & ROOT
		
		if(requesterUser.getRole().isAdmin()) {
			
			if(juser.isPresentInGroup()) {
				this.setGroupsJBL(juser.getInGroups());
			}

			
		}
		
		
		return read(null, requesteurGuid);
		
	}


	
}



