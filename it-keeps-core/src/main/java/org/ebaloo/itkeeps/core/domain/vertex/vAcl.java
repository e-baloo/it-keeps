

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
public class vAcl extends vBase {

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
		
		setEdges(this.getGraph(), vAcl.class, this, list, DirectionType.CHILD, eAclRelation.class, true);
	}

	protected void _setChildObjects(List<jBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setChildObjects(list.stream().map(e -> get(graph, vBaseChildAcl.class, e, true)).collect(Collectors.toList())); 
	}

	
	/*
	 * ACL DATA
	 */
	
	protected enAclData getAclData() {
		return enAclData.valueOf(this.getEdgeByClassesNames(vAclData.class, DirectionType.PARENT, false, eAclNoTraverse.class).getName());
	}
	
	protected void setAclData(final enAclData aclData) {
		setEdges(this.getGraph(), vAcl.class, this, vAclData.get(getGraph(), vAclData.class, aclData.name()), DirectionType.PARENT, eAclNoTraverse.class, false);
	}

	
	/*
	 * ACL ADMIN
	 */
	
	protected List<enAclAdmin> getAclAdmin() {
		
		return this.getEdgesByClassesNames(
				vAclData.class, 
				DirectionType.PARENT, 
				true, 
				eAclNoTraverse.class)
					.stream().map(e -> enAclAdmin.valueOf(getName())).collect(Collectors.toList());
	}
	
	protected void setAclAdmin(List<enAclAdmin> list) {
		if(list == null)
			list = new ArrayList<enAclAdmin>();
		
		setEdges(
				this.getGraph(),
				vAcl.class, 
				this, 
				list.stream().map(e -> vAclAdmin.get(getGraph(), vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}

	
	
	/*
	 * OWNER
	 */
	
	@DatabaseProperty(name = jAcl.OWNER, type = OType.BOOLEAN)
	protected void setOwner(Boolean enable) {
		this.setProperty(jAcl.OWNER, enable);
	}

	public Boolean isOwner() {
		return (Boolean) this.getProperty(jAcl.OWNER);
	}

	
	
	
	
	

	
	// API
	
	public vAcl(final jAcl j) {
		this(j, true);
	}
	
	protected vAcl(final jAcl j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();
		
		this._setChildObjects(j.getChildObjects());
		this.setAclData(j.getAclData());
		this.setAclAdmin(j.getAclAdmin());
		
		this.reload();

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public <T extends jBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new jAcl();
		
		if(!(j instanceof jAcl))
			throw new RuntimeException("TODO"); //TODO
		
		
		// TODO SECURITY
		
		
		super.read(j, requesteurGuid);

		jAcl jgroup = (jAcl) j;
		
		jgroup.setChildObjects(this.getChildObjects().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		jgroup.setAclData(this.getAclData());
		jgroup.setAclAdmin(this.getAclAdmin());
		
		return j;
	}
	
	@Override
	public <T extends jBase> T update(T j, Guid requesteurGuid) {
		
		if(!(j instanceof jAcl))
			throw new RuntimeException("TODO"); //TODO

		vUser requesterUser = vUser.get(this.getGraph(), vUser.class, requesteurGuid, false);

		switch(requesterUser.getRole()) {

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
		

		super.update(j, requesteurGuid);
		
		jAcl jacl = (jAcl) j;

		
		this._setChildObjects(jacl.getChildObjects());
		this.setAclData(jacl.getAclData());
		this.setAclAdmin(jacl.getAclAdmin());

		
		return read(null, requesteurGuid);
		
	}
}



