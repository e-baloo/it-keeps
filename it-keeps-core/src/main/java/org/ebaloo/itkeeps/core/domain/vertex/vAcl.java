

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
public final class vAcl extends vBase {

	protected vAcl() {
		super();
	}
	
	
	/*
	 * CHILD OBJECTS
	 */
	
	private List<jBaseLight> getChildObjects() {
		return this.getEdges(vBaseChildAcl.class, DirectionType.CHILD, true, eAclRelation.class).stream()
			.map(vBase::getJBaseLight).collect(Collectors.toList());
	}
	
	private void setChildObjects(List<jBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(
				graph, 
				vAcl.class, 
				this, 
				vBaseChildAcl.class, 
				list.stream().map(e -> get(graph, vBaseChildAcl.class, e, true)).collect(Collectors.toList()), 
				DirectionType.CHILD, 
				eAclRelation.class, 
				true);
		}

	
	/*
	 * ACL OWNER
	 */
	
	private enAclOwner getOwner() {
		return enAclOwner.valueOf(this.getEdge(vAclOwner.class, DirectionType.PARENT, false, eAclNoTraverse.class).getName());
	}
	
	private void setOwner(final enAclOwner aclOwner) {
		setEdges(this.getGraph(), vAcl.class, this, vAclOwner.class, vAclOwner.get(getGraph(), vAclOwner.class, aclOwner.name()), DirectionType.PARENT, eAclNoTraverse.class, false);
	}
	
	/*
	 * ACL DATA
	 */
	
	private List<enAclData> getAclData() {
		return this.getEdges(
				vAclData.class, 
				DirectionType.PARENT, 
				true, 
				eAclNoTraverse.class)
					.stream().map(e -> enAclData.valueOf(e.getName())).collect(Collectors.toList());
	}
	
	private void setAclData(List<enAclData> list) {
		if(list == null)
			list = new ArrayList<>();
		
		setEdges(
				this.getGraph(),
				vAcl.class, 
				this,
				vAclData.class,
				list.stream().map(e -> vAclData.get(getGraph(), vAclData.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}

	
	/*
	 * ACL ADMIN
	 */
	
	private List<enAclAdmin> getAclAdmin() {
		return this.getEdges(
				vAclAdmin.class, 
				DirectionType.PARENT, 
				true, 
				eAclNoTraverse.class)
					.stream().map(e -> enAclAdmin.valueOf(e.getName())).collect(Collectors.toList());
	}
	
	private void setAclAdmin(List<enAclAdmin> list) {
		if(list == null)
			list = new ArrayList<>();
		
		setEdges(
				this.getGraph(),
				vAcl.class, 
				this,
				vAclAdmin.class,
				list.stream().map(e -> vAclAdmin.get(getGraph(), vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}

	
	
	
	
	
	
	

	
	// API
	
	vAcl(final jAcl j) {
		super(j);
		
		try {
			this.update(j);
			this.setChildObjects(j.getChildObjects());

		} catch (Exception e) {
			this.delete();
			throw e;
		}

	}

	

	final jAcl read() {
		
		jAcl j = new jAcl();
		
		this.readBase(j);
		
		
		j.setChildObjects(this.getChildObjects());
		j.setAclData(this.getAclData());
		j.setAclAdmin(this.getAclAdmin());
		j.setOwner(this.getOwner());
		
		return j;
	}
	
	
	
	final void update(jAcl jacl) {
		this.setAclData(jacl.getAclData());
		this.setAclAdmin(jacl.getAclAdmin());
		this.setOwner(jacl.getOwner());
	}


}



