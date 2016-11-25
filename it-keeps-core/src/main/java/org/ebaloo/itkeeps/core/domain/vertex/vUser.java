
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.oRID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.jUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


@DatabaseVertrex()
public final class vUser extends vBaseChildAcl {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vUser.class);

	
	vUser() { };
	
	
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
	
	private vUser(final jUser j, final SecurityAcl sAcl) {
		super(j, false);
		
		this._update(j, sAcl);

		this.setEnable(Boolean.TRUE);
	}

	vUser(jUser j) {
		super(j, false);
		
		this.setRole(enAclRole.GUEST);
		this.setEnable(Boolean.TRUE);
	}

	private void _update(final jUser j, SecurityAcl sAcl) {

		this._updateRole(j, sAcl);
		
		this._updateInGroup(j, sAcl);
		
		this._updateAclAdmin(j, sAcl);
	}
	
	private void _updateAclAdmin(final jUser j, SecurityAcl sAcl) {

		if(!j.isPresentAclAdmin())
			return;
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			return;

		if(!sAcl.isAdminDeleguat())
			return;
	
		this.setAclAdmin(j.getAclAdmin());
	}
	
	private void _updateRole(final jUser j, SecurityAcl sAcl) {

		if(!j.isPresentRole())
			return;
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			return;
		
		enAclRole role = j.getRole();

		if(role.equals(this.getRole()))
			return;
		
		if(role.isRoot() && !sAcl.isRoleRoot())
			return;
		
		if(!sAcl.isAdminDeleguat())
			return;
	
		this.setRole(role);
	}
	
	private void _updateInGroup(final jUser j, SecurityAcl sAcl) {

		if(!j.isPresentInGroup())
			return;
			
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			return;

		List<jBaseLight> list = new ArrayList<jBaseLight>();
		
		for(jBaseLight jbl : j.getInGroups()) {
			if(SecurityFactory.getSecurityAcl(new oRID(this), new oRID(jbl)).isAdminUpdateGroup()) {
				list.add(jbl);
			}
		}
		
		this.setGroupsJBL(list);
	}
	
	public jUser read(Guid requesteurGuid) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(new oRID(requesteurGuid), new oRID(this));

		if(sAcl.isRoleGuest())
			throw new RuntimeException("Error : requester is GUEST");
		
		if((!sAcl.isRoleRoot() || !sAcl.isRoleAdmin()) && !this.getGuid().equals(requesteurGuid) )
			throw new RuntimeException("Error : requester is USER and read user is not himself");

		
		
		
		jUser j = new jUser();
		
		this.readBaseStandard(j, requesteurGuid);

		j.setRole(this.getRole());
		
		j.setInGroups(this.getGroups().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	public static jUser create(jUser j, Guid requesteurGuid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(new oRID(requesteurGuid), null);
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw new RuntimeException("Error : user is GUEST or USER "); //TODO

		if(!sAcl.isAdminUserCreate())
			throw new RuntimeException("Error : user have not right to create 'User'" ); //TODO
		
		vUser user = new vUser(j, sAcl);
		
		return user.read(requesteurGuid);
	}

	public jUser update(jUser j, Guid requesteurGuid) {
		return this.update(j, requesteurGuid, false);
	}

	private jUser update(jUser j, Guid requesteurGuid, boolean force) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(new oRID(requesteurGuid), new oRID(this));

		if(sAcl.isRoleGuest())
			throw new RuntimeException("Error : user is GUEST"); //TODO
		
		if(!this.getGuid().equals(requesteurGuid) && !sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw new RuntimeException("TODO"); //TODO

		
		this._updateBaseStandard(j, requesteurGuid, force);

		this._update(j, sAcl);
		
		return read(requesteurGuid);
		
	}


	
}



