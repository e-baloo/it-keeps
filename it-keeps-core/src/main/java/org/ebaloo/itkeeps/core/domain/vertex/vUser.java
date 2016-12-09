
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eCredentialToUser;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;

import org.ebaloo.itkeeps.api.model.jUser;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;

@DatabaseVertrex()
public final class vUser extends vBaseChildAcl {


	vUser() {
		;
	};

	vUser(final jUser j) {
		super(j);

		try {

			this.update(j);

		} catch (Exception e) {
			this.delete();
			throw e;
		}

	}

	vUser(final jUser j, boolean force) {
		super(j);

		try {
			this.setRole(enAclRole.GUEST);

		} catch (Exception e) {
			this.delete();
			throw e;
		}

	}

	private List<enAclAdmin> getAclAdmin() {
		return this.getEdges(vAclAdmin.class, DirectionType.PARENT, true, eAclNoTraverse.class).stream()
				.map(e -> enAclAdmin.valueOf(e.getName())).collect(Collectors.toList());
	}

	private List<jBaseLight> getAclGroups() {
		return this.getEdges(vAclGroup.class, DirectionType.PARENT, false, eAclNoTraverse.class).stream()
				.map(vBase::getJBaseLight).collect(Collectors.toList());
	}

	private List<jBaseLight> getCredentials() {
		return this.getEdges(vCredential.class, DirectionType.CHILD, false, eCredentialToUser.class).stream()
				.map(vBase::getJBaseLight).collect(Collectors.toList());
	}

	/*
	final List<jBaseLight> getCredential() {
		return this.getEdges(vCredential.class, DirectionType.CHILD, false, eCredentialToUser.class).stream()
				.map(e -> getJBaseLight(e)).collect(Collectors.toList());
	}
	*/
	
	private List<jBaseLight> getGroups() {
		return this.getEdges(vGroup.class, DirectionType.PARENT, false, eInGroup.class).stream()
				.map(vBase::getJBaseLight).collect(Collectors.toList());

	}

	final enAclRole getRole() {
		vAclRole role = this.getEdge(vAclRole.class, DirectionType.PARENT, false, eAclNoTraverse.class);
		if (role == null)
			return enAclRole.GUEST;
		return enAclRole.valueOf(role.getName());
	}

	final jUser read() {

		jUser j = new jUser();

		this.readBaseStandard(j);

		j.setCredentials(this.getCredentials());
		j.setAclAdmin(this.getAclAdmin());
		j.setRole(this.getRole());
		j.setGroups(this.getGroups());
		j.setAclGroups(this.getAclGroups());

		this.readAcl(j);
		
		return j;
	}

	private void setAclAdmin(List<enAclAdmin> list) {
		if (list == null)
			list = new ArrayList<>();

		setEdges(this.getGraph(),
				vUser.class, this, vAclAdmin.class, list.stream()
						.map(e -> vAclAdmin.get(getGraph(), vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT, eAclNoTraverse.class, false);
	}

	private void setAclGroups(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(graph, vUser.class, this, vAclGroup.class,
				list.stream().map(e -> get(graph, vAclGroup.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT, eAclNoTraverse.class, false);
	}

	/*
	private final void setCredentials(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(graph, vUser.class, this, vCredential.class,
				list.stream().map(e -> get(graph, vCredential.class, e, false)).collect(Collectors.toList()),
				DirectionType.CHILD, eCredentialToUser.class, false);
	}
	*/

	private void setGroups(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(graph, vUser.class, this, vGroup.class,
				list.stream().map(e -> get(graph, vGroup.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT, eInGroup.class, false);

	}

	final void setRole(final enAclRole aclRole) {
		setEdges(this.getGraph(), vUser.class, this, vAclRole.class,
				vAclRole.get(this.getGraph(), vAclRole.class, aclRole.name()), DirectionType.PARENT,
				eAclNoTraverse.class, false);
	}

	final void update(final jUser j) {

		this.updateBaseStandard(j);

		if (j.isPresentRole())
			this.setRole(j.getRole());

		if (j.isPresentGroups())
			this.setGroups(j.getGroups());

		if (j.isPresentAclAdmin())
			this.setAclAdmin(j.getAclAdmin());

		/*
		if (j.isPresentCredentials())
			this.setCredentials(j.getCredentials());
		*/

		if (j.isPresentAclGroups())
			this.setAclGroups(j.getAclGroups());
		
	}

}
