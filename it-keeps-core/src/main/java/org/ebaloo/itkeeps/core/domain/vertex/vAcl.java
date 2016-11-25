

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
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
	 * CHILD GROUP
	 */
	
	protected List<vBaseChildAcl> getChildObjects() {
		return this.getEdgesByClassesNames(vBaseChildAcl.class, DirectionType.CHILD, true, eAclRelation.class);
	}
	
	protected void setChildObjects(List<vBaseChildAcl> list) {
		if(list == null)
			list = new ArrayList<vBaseChildAcl>();
		
		setEdges(this.getGraph(), vAcl.class, this, vBaseChildAcl.class, list, DirectionType.CHILD, eAclRelation.class, true);
	}

	protected void _setChildObjects(List<jBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setChildObjects(list.stream().map(e -> get(graph, vBaseChildAcl.class, e, true)).collect(Collectors.toList())); 
	}

	
	/*
	 * ACL OWNER
	 */
	
	protected enAclOwner getOwner() {
		return enAclOwner.valueOf(this.getEdgeByClassesNames(vAclOwner.class, DirectionType.PARENT, false, eAclNoTraverse.class).getName());
	}
	
	protected void setOwner(final enAclOwner aclOwner) {
		setEdges(this.getGraph(), vAcl.class, this, vAclOwner.class, vAclOwner.get(getGraph(), vAclOwner.class, aclOwner.name()), DirectionType.PARENT, eAclNoTraverse.class, false);
	}
	
	/*
	 * ACL DATA
	 */
	
	protected List<enAclData> getAclData() {
		return this.getEdgesByClassesNames(
				vAclData.class, 
				DirectionType.PARENT, 
				true, 
				eAclNoTraverse.class)
					.stream().map(e -> enAclData.valueOf(e.getName())).collect(Collectors.toList());
	}
	
	protected void setAclData(List<enAclData> list) {
		if(list == null)
			list = new ArrayList<enAclData>();
		
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
				vAcl.class, 
				this,
				vAclAdmin.class,
				list.stream().map(e -> vAclAdmin.get(getGraph(), vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}

	
	
	
	
	
	
	

	
	// API
	
	public vAcl(final jAcl j) {
		this(j, true);
	}
	
	protected vAcl(final jAcl j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();
		
		this._update(j);

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	

	public jAcl read(Guid requesteurGuid) {
		
		jAcl j = new jAcl();
		
		this.readBase(j, requesteurGuid);
		
		
		j.setChildObjects(this.getChildObjects().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		j.setAclData(this.getAclData());
		j.setAclAdmin(this.getAclAdmin());
		j.setOwner(this.getOwner());
		
		return j;
	}
	
	public jAcl update(jAcl j, Guid requesteurGuid) {
		
		vUser requesterUser = vUser.get(this.getGraph(), vUser.class, requesteurGuid, false);

		switch(requesterUser.getRole().value()) {

		case ROOT:
			break;

		case ADMIN:
			//TODO Check it requesterUser have the right to update this
			break;
			
		case USER:
		case GUEST:
		default:
			throw new RuntimeException("TODO"); //TODO
		}
		
		this._updateBase(j, requesteurGuid, false);
		
		this._update((jAcl) j);

		return read(requesteurGuid);
	}
	
	
	private final void _update(jAcl jacl) {
		this._setChildObjects(jacl.getChildObjects());
		this.setAclData(jacl.getAclData());
		this.setAclAdmin(jacl.getAclAdmin());
		this.setOwner(jacl.getOwner());
	}
	
}



