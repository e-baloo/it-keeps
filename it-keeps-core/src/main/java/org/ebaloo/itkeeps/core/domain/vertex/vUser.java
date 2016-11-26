
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.ebaloo.itkeeps.api.model.jUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


@DatabaseVertrex()
public final class vUser extends vBaseChildAcl {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vUser.class);

	
	vUser() { ; };
	
	
	/*
	 * GROUPS
	 */
	
	private List<jBaseLight> getGroups() {
		return this.getEdges(
				vGroup.class,
				DirectionType.PARENT,
				false,
				eInGroup.class).stream().map(e -> getJBaseLight(e)).collect(Collectors.toList());
		
	}
	
	private void setGroups(List<jBaseLight> list) {
		
		if(list == null)
			list = new ArrayList<jBaseLight>();
		
		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setEdges(
				graph,
				vUser.class, 
				this, 
				vGroup.class, 
				list.stream().map(e -> get(graph, vGroup.class, e, false)).collect(Collectors.toList()), 
				DirectionType.PARENT, 
				eInGroup.class, false);

	}

	/*
	 * CREDENTIALS
	 */
	
	private List<jBaseLight> getCredentials() {
		return this.getEdges(
				vCredential.class,
				DirectionType.CHILD,
				false,
				eCredentialToUser.class).stream().map(e -> getJBaseLight(e)).collect(Collectors.toList());
	}
	

	private void setCredentials(List<jBaseLight> list) {
		
		if(list == null)
			list = new ArrayList<jBaseLight>();
		
		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setEdges(
				graph,
				vUser.class,
				this,
				vCredential.class,
				list.stream().map(e -> get(graph, vCredential.class, e, false)).collect(Collectors.toList()),
				DirectionType.CHILD,
				eCredentialToUser.class,
				false);
	}
	
	
	/*
	 * ROLE
	 */
	
	enAclRole getRole() {
		vAclRole role = this.getEdge(vAclRole.class, DirectionType.PARENT, false, eAclNoTraverse.class);
		if(role == null)
			return enAclRole.GUEST;
		return enAclRole.valueOf(role.getName());
	}
	
	void setRole(final enAclRole aclRole) {
		setEdges(
				this.getGraph(),
				vUser.class,
				this,
				vAclRole.class,
				vAclRole.get(
						this.getGraph(),
						vAclRole.class,
						aclRole.name()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}
	
	
	/*
	 * ACL ADMIN
	 */
	
	protected List<enAclAdmin> getAclAdmin() {
		return this.getEdges(
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
		super(j);

		try {
		
		this.updateImplem(j, sAcl);

	} catch (Exception e) {
		this.delete();
		throw e;
	}

	}

	vUser(jUser j) {
		super(j);
		
		try {
		this.setRole(enAclRole.GUEST);
		
	} catch (Exception e) {
		this.delete();
		throw e;
	}

	}

	private void updateImplem(final jUser j, SecurityAcl sAcl) {
		this.updateRole(j, sAcl);
		this.updateGroups(j, sAcl);
		this.updateAclAdmin(j, sAcl);
		this.updateCredentials(j, sAcl);
	}
	
	private void updateCredentials(jUser j, SecurityAcl sAcl) {
		if(!j.isPresentCredentials())
			return;
		this.setCredentials(j.getCredentials());
	}

	private void updateAclAdmin(final jUser j, SecurityAcl sAcl) {
		if(!j.isPresentAclAdmin())
			return;
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if(!sAcl.isAdminDelegate())
			throw ExceptionPermission.NOT_DELEGATE;
		this.setAclAdmin(j.getAclAdmin());
	}
	
	private void updateRole(final jUser j, SecurityAcl sAcl) {
		if(!j.isPresentRole())
			return;
		enAclRole role = j.getRole();
		if(role.equals(this.getRole()))
			return;
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;
		if(role.isRoot() && !sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;
		if(!sAcl.isAdminDelegate())
			throw ExceptionPermission.NOT_DELEGATE;
		this.setRole(role);
	}
	
	private void updateGroups(final jUser j, SecurityAcl sAcl) {
		if(!j.isPresentGroups())
			return;
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			return;
		if(!sAcl.isAdminUpdateGroup())
			throw ExceptionPermission.NOT_GROUP_UPDATE;
		this.setGroups(j.getGroups());
	}
	
	public static final jUser read(Rid requesteurRid, Rid user) {

		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, user);

		if(sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		if((!sAcl.isRoleRoot() || !sAcl.isRoleAdmin()) && !user.equals(requesteurRid) )
			throw ExceptionPermission.IS_USER;

		return vUser.get(null, vUser.class, user, false).read();
	}
	
	private jUser read() {
		jUser j = new jUser();
		this.readBaseStandard(j);
		j.setRole(this.getRole());
		j.setGroups(this.getGroups());
		j.setCredentials(this.getCredentials());
		
		return j;
	}
	
	public static final jUser create(Rid requesteurRid, jUser j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, null);
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_GUEST_OR_USER;

		if(!sAcl.isAdminUserCreate())
			throw ExceptionPermission.NOT_USER_CREATE ;
		
		vUser user = new vUser(j, sAcl);
		
		return vUser.read(requesteurRid, user.getRid());
	}

	public static final jUser update(Rid requesteurRid, jUser j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, j.getRid());

		if(sAcl.isRoleGuest())
			throw ExceptionPermission.IS_GUEST;
		
		if(!j.getRid().equals(requesteurRid) && !sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw ExceptionPermission.IS_USER;

		vUser user = vUser.get(null, vUser.class, j.getRid(), false);
		
		user.checkVersion(j);
		user.updateBaseStandard(j);
		user.updateImplem(j, sAcl);
		
		return vUser.read(requesteurRid, j.getRid());
		
	}


	
}



