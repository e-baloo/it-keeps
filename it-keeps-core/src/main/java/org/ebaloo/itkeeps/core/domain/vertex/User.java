
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.annotation.ApplicationRolesAllowed.SecurityRole;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.api.model.JCredential;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.ModelFactory.ModelClass;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.TraverseInGroup;
import org.ebaloo.itkeeps.core.tools.SecurityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.JUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
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
	
	

	/*
	 * IN_GROUP
	 */
	
	public List<Group> getInGroup() {
		return this.getEdgesByClassesNames(ModelFactory.get(Group.class), DirectionType.CHILD, false, TraverseInGroup.class);
	}
	
	public void setInGroup(List<Group> list) {
		setEdges(this.getGraph(), ModelFactory.get(Group.class), this, list, DirectionType.CHILD, TraverseInGroup.class, false);
	}

	private void setInGroupJBL(List<JBaseLight> list) {
		
		if(list == null) {
			list = new ArrayList<JBaseLight>();
		}
		
		// Optimization
		ModelClass<Group> mc = ModelFactory.get(Group.class);
		OrientBaseGraph graph = this.getGraph();
		
		setInGroup(list.stream().map(e -> get(graph, mc, e, false)).collect(Collectors.toList())); 
	}

	
	/*
	 *  ID
	 */
	

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
	
	
	/*
	 *   PASSWORD
	 */
	

	@DatabaseProperty(name = JUser.PASSWORD)
	public String getPassword()  {
		return this.getProperty(JUser.PASSWORD);
	}

	public void setPassowrd(final String value) {
		
		this.setProperty(JUser.PASSWORD, value);
	}
	
	
	

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
	
	public User(final JUser j) {
		this(j, true);
	}
	
	protected User(final JUser j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();

		if(User.getById(this.getGraph(), j.getUserId()) != null)
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
		
		
		User requesterUser = User.get(this.getGraph(), ModelFactory.get(User.class), requesteurGuid, false);

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


	
}



