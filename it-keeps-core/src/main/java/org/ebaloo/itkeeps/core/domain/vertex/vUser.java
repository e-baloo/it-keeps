
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.jUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


@DatabaseVertrex()
public final class vUser extends vBaseChildAcl {

	private static Logger logger = LoggerFactory.getLogger(vUser.class);

	
	protected vUser() {
		super();
	}
	
	/*
	 * IN_GROUP
	 */
	
	protected List<vGroup> getGroups() {
		return this.getEdgesByClassesNames(vGroup.class, DirectionType.PARENT, false, eInGroup.class);
	}
	
	protected void setGroups(List<vGroup> list) {
		setEdges(this.getGraph(), vUser.class, this, vGroup.class, list, DirectionType.PARENT, eInGroup.class, false);
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
	
	protected List<vCredential> getCredentials() {
		return this.getEdgesByClassesNames(vCredential.class, DirectionType.CHILD, false, eCredentialToUser.class);
	}
	
	protected void _setCredentials(List<vCredential> list) {
		setEdges(this.getGraph(), vUser.class, this, vCredential.class, list, DirectionType.CHILD, eCredentialToUser.class, false);
	}

	protected void setCredentials(List<jBaseLight> list) {
		
		if(list == null) {
			list = new ArrayList<jBaseLight>();
		}
		
		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		this._setCredentials(list.stream().map(e -> get(graph, vCredential.class, e, false)).collect(Collectors.toList())); 
	}
	
	
	/*
	 * ROLE
	 */
	
	
	enAclRole getRole() {
		
		vAclRole role = this.getEdgeByClassesNames(vAclRole.class, DirectionType.PARENT, false, eAclNoTraverse.class);
		
		if(role == null)
			return enAclRole.GUEST;
		
		return enAclRole.valueOf(role.getName());
	}
	
	void setRole(final enAclRole aclRole) {
		logger.info("ROLE ==== " + aclRole);
		
		setEdges(this.getGraph(), vUser.class, this, vAclRole.class, vAclRole.get(getGraph(), vAclRole.class, aclRole.name()), DirectionType.PARENT, eAclNoTraverse.class, false);
	}
	
	
	/*
	 * ACL ADMIN
	 */
	
	protected List<enAclAdmin> getAclAdmin() {
		return this.getEdgesByClassesNames(
				vAclAdmin.class, 
				DirectionType.PARENT, 
				true, 
				eAclNoTraverse.class)
					.stream().map(e -> enAclAdmin.valueOf(e.getName())).collect(Collectors.toList());
	}
	
	protected void setAclAdmin(List<enAclAdmin> list) {
		if(list == null)
			list = new ArrayList<enAclAdmin>();
		
		setEdges(
				this.getGraph(),
				vUser.class, 
				this, 
				vAclAdmin.class,
				list.stream().map(e -> vAclAdmin.get(getGraph(), vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}
	
	
	
	// API
	
	/*
	private vUser(final jUser j, Guid requesteurGuid) {
		this(j, true, requesteurGuid);
	}
	*/
	
	private vUser(final jUser j, final SecurityAcl sAcl) {
		super(j, false);
		
		this.commit();
		this.reload();

		this._update(j, sAcl);

		this.setEnable(Boolean.TRUE);
	}

	
	
	
	
	vUser(jUser j) {
		super(j, false);
		
		this.commit();
		this.reload();
		
		this.setRole(enAclRole.GUEST);
		this.setEnable(Boolean.TRUE);
	}

	private void _update(final jUser j, SecurityAcl sAcl) {

		if(j.isPresentRole()) {
			
			enAclRole role = j.getRole();

			if(!role.equals(this.getRole())) {
			
			if(role.isRoot() && !sAcl.isRoot())
				throw new RuntimeException(); //TODO
			
			if(role.isAdmin() && !sAcl.isAdminDeleguat())
				throw new RuntimeException(); // TODO
		
			this.setRole(role);
			}
		}

		if(j.isPresentInGroup() && sAcl.isAdminUpdateGroup())
				this.setGroupsJBL(j.getInGroups());
			

		if(j.isPresentAclAdmin()) // TODO
			this.setAclAdmin(j.getAclAdmin());

		
		

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
		
		juser.setRole(this.getRole());
		
		juser.setInGroups(this.getGroups().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	public static <T extends jBase> T create(T j, Guid requesteurGuid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(null, requesteurGuid, null);
		
		if(sAcl.isGuest() || sAcl.isUser())
			throw new RuntimeException("Error : user is GUEST or USER "); //TODO

		// TODO Check Security
		
		vUser user = new vUser((jUser) j, sAcl);
		
		return user.read(null, requesteurGuid);
	}
	
	@Override
	public <T extends jBase> T update(T j, Guid requesteurGuid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(this.getGraph(), requesteurGuid, this);
		
		if(sAcl.isGuest())
			throw new RuntimeException("Error : user is GUEST"); //TODO
		
		if(sAcl.isUser() && !this.getGuid().equals(requesteurGuid))
			throw new RuntimeException("TODO"); //TODO

		
		if(!(j instanceof jUser))
			throw new RuntimeException("TODO"); //TODO


		super.update(j, requesteurGuid);


		
		this._update((jUser) j, sAcl);
		
		return read(null, requesteurGuid);
		
	}


	
}



