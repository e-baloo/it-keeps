

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.RID;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
public final class vGroup extends vBaseChildAcl {

	protected vGroup() {
		super();
	}
	
	/*
	 * PARENT
	 */

	final jBaseLight getParent() {
		vGroup child = this.getEdge(vGroup.class, DirectionType.CHILD, false, eInGroup.class);
		return child == null ? null : getJBaseLight(child);
	}

	
	final void setParent(final jBaseLight group) {
		vGroup child = get(this.getGraph(), vGroup.class, group, false);
		setEdges(this.getGraph(), vGroup.class, this, vGroup.class, child, DirectionType.CHILD, eInGroup.class, false);
	}

	
	/*
	 * CHILDS
	 */
	
	private final List<jBaseLight> getChilds() {
		return this.getEdges(vGroup.class, DirectionType.PARENT, false, eInGroup.class).stream()
				.map(e -> getJBaseLight(e)).collect(Collectors.toList());

	}
	
	private final void setChilds(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(graph, vGroup.class, this, vGroup.class,
				list.stream().map(e -> get(graph, vGroup.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT, eInGroup.class, false);

	}
	
	
	// API
	
	public vGroup(final jGroup j) {
		super(j);

		try {
		this.setParent(j.getParent());
		this.setChilds(j.getChilds());
	} catch (Exception e) {
		this.delete();
		throw e;
	}
	}

	

	public jGroup read(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, RID.get(this));
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw new RuntimeException("Error : user is GUEST or USER / " + sAcl.toString()); //TODO

		
		jGroup j = new jGroup();
		
		
		// TODO SECURITY
		
		this.readBaseStandard(j);
		
		jGroup jgroup = (jGroup) j;
		
		jgroup.setParent(this.getParent());
		jgroup.setChilds(this.getChilds());
		
		return j;
	}

	public jGroup update(jGroup j, Rid requesteurRid) {
		return this.update(j, requesteurRid, false);
	}
	
	public jGroup update(jGroup j, Rid requesteurRid, boolean force) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, RID.get(this));
		
		if(!sAcl.isRoleRoot() || !sAcl.isRoleAdmin())
			throw new RuntimeException("Error : user is GUEST or USER "); //TODO

		
		if(!(j instanceof jGroup))
			throw new RuntimeException("TODO"); //TODO

			
		this.checkVersion(j);

		this.updateBaseStandard(j);
			
		jGroup jgroup = (jGroup) j;
		
		if(jgroup.isPresentParent())
			this.setParent(jgroup.getParent());

		if(jgroup.isPresentChilds())
			this.setChilds(jgroup.getChilds());
	
		
		return read(requesteurRid);
		
	}
}



